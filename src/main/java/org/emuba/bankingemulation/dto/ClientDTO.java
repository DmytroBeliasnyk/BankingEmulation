package org.emuba.bankingemulation.dto;

import lombok.Data;
import org.emuba.bankingemulation.models.Account;

import java.util.List;

@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private List<AccountDTO> accounts;

    private ClientDTO(Long id, String name, String surname, String email, List<AccountDTO> accounts) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.accounts = accounts;
    }

    public static ClientDTO of(Long id, String name, String surname,
                               String email, List<AccountDTO> list) {
        return new ClientDTO(id, name, surname, email, list);
    }
}
