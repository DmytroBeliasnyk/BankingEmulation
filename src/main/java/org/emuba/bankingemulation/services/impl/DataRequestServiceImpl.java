package org.emuba.bankingemulation.services.impl;

import org.emuba.bankingemulation.dto.DataRequestDTO;
import org.emuba.bankingemulation.enums.DataType;
import org.emuba.bankingemulation.models.DataRequest;
import org.emuba.bankingemulation.repositories.DataRepository;
import org.emuba.bankingemulation.services.DataRequestService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataRequestServiceImpl implements DataRequestService {
    private final DataRepository dataRepository;
    private final ClientServiceImpl clientService;

    public DataRequestServiceImpl(DataRepository dataRepository, ClientServiceImpl clientService) {
        this.dataRepository = dataRepository;
        this.clientService = clientService;
    }

    @Override
    @Transactional
    public void add(String login, DataType dataType, Long transactionId) {
        dataRepository.save(DataRequest.of(clientService.findClientByLogin(login),
                dataType, transactionId));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        dataRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataRequestDTO> getAll() {
        return dataRepository.findAll()
                .stream()
                .map(DataRequest::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DataRequest find(Long id) {
        return dataRepository.findById(id).get();
    }
}
