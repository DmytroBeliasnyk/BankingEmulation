package org.emuba.bankingemulation.dto;

import lombok.Data;

@Data
public class ClientDTO {
    private String name;
    private String surname;
    private String email;

    private ClientDTO(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public static ClientDTO of(String name, String surname, String email) {
        return new ClientDTO(name, surname, email);
    }
}
