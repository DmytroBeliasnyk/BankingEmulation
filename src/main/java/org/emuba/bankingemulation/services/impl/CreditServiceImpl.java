package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.dto.CreditDTO;
import org.emuba.bankingemulation.enums.ClientRequestType;
import org.emuba.bankingemulation.enums.TypeCurrency;
import org.emuba.bankingemulation.models.Credit;
import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.repositories.CreditRepository;
import org.emuba.bankingemulation.services.CreditService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@EnableScheduling
@Service
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;
    private final ClientServiceImpl clientService;
    private final AccountServiceImpl accountService;
    private final SupportingMethodsForControllers supMethod;
    private final MailService mailService;

    public CreditServiceImpl(CreditRepository creditRepository, ClientServiceImpl clientService,
                             AccountServiceImpl accountService, SupportingMethodsForControllers supMethod, MailService mailService) {
        this.creditRepository = creditRepository;
        this.clientService = clientService;
        this.accountService = accountService;
        this.supMethod = supMethod;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public CreditDTO save(String login, LocalDate startDate, LocalDate endDate, BigDecimal amount, TypeCurrency currency) {
        CustomClient client = clientService.findClientByLogin(login);

        Credit credit = Credit.of(startDate, endDate, amount, currency);
        client.addCredit(credit);

        return creditRepository.save(credit).toDTO();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditDTO> findAllCredits(String login) {
        return creditRepository.findAllByClient(clientService.findClientByLogin(login))
                .stream()
                .map(Credit::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void updateCreditBalance(Long creditId, BigDecimal amount) {
        creditRepository.updateBalance(creditId, amount);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateCreditBalance() {
        for (var credit : creditRepository.findAll()) {
            BigDecimal convertedAmount = credit.getOnePayment();
            if (!credit.getCurrency().equals(TypeCurrency.UAH))
                convertedAmount = supMethod.converter(credit.getCurrency().name(), "UAH", credit.getAmount());

            BigDecimal accountBalance = accountService.findAccount(TypeCurrency.UAH, credit.getClient().getLogin())
                    .get()
                    .getBalance();

            accountService.updateBalance(credit.getClient().getId(), TypeCurrency.UAH,
                    accountBalance.subtract(credit.getAmount()));

            BigDecimal newCreditBalance = credit.getAmount().subtract(credit.getOnePayment());
            if (newCreditBalance.compareTo(BigDecimal.ZERO) <= 0)
                deleteCredit(credit.getId());

            updateCreditBalance(credit.getId(), newCreditBalance);
            mailService.sendEmail(credit.getClient().getEmail(), ClientRequestType.CREDIT_INFO,
                    creditRepository.findById(credit.getId()).toString());
        }
    }

    @Override
    @Transactional
    public void deleteCredit(Long creditId) {
        creditRepository.deleteById(creditId);
    }
}
