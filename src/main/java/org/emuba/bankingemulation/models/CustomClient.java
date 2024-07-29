package org.emuba.bankingemulation.models;

import jakarta.persistence.*;
import lombok.*;
import org.emuba.bankingemulation.dto.AccountDTO;
import org.emuba.bankingemulation.dto.ClientDTO;
import org.emuba.bankingemulation.enums.UserRole;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
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
    private Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Credit> credits = new HashSet<>();

    private CustomClient(String name, String surname, String email,
                         String login, String password, UserRole role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.login = login;
        this.password = password;
        this.role = role;
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

    public void addAccount(Account account) {
        if (accounts.add(account)) account.setClient(this);
    }

    public void addCredit(Credit credit) {
        if (credits.add(credit)) credit.setClient(this);
    }

    @Override
    public String toString() {
        return "CustomClient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", accounts=" + accounts +
                ", credits=" + credits +
                '}';
    }
}
