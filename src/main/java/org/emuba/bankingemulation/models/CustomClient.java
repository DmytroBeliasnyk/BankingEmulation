package org.emuba.bankingemulation.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.dto.ClientDTO;
import org.emuba.bankingemulation.enums.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
public class CustomClient {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private String email;

    private String login;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "from", cascade = CascadeType.ALL)
    private List<HistoryOfTransaction> historyFrom = new ArrayList<>();

    @OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
    private List<HistoryOfTransaction> historyTo = new ArrayList<>();

    private CustomClient(String name, String surname, String email,
                         String login, String password, UserRole role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public void addAccount(Account Account) {
        if (!accounts.contains(Account)) {
            accounts.add(Account);
            Account.setClient(this);
        }
    }

    public static CustomClient of(String name, String surname, String email,
                                  String login, String password, UserRole role) {
        return new CustomClient(name, surname, email, login, password, role);
    }

    public ClientDTO toDTO() {
        List<AccountDTO> list = accounts.stream()
                .map(Account::toDTO)
                .toList();
        return ClientDTO.of(id, name, surname, email, list);
    }
}
