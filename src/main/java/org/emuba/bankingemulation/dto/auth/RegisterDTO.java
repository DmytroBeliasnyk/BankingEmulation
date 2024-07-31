package org.emuba.bankingemulation.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterDTO {
    private String name;
    private String surname;
    private String email;
    private String login;
    private String password;
}
