package org.emuba.bankingemulation.dto.auth;

import lombok.Data;

@Data
public class RegisterDTO {
    private String name;
    private String surname;
    private String email;
    private String login;
    private String password;
}
