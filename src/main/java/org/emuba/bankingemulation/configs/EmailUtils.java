package org.emuba.bankingemulation.configs;

import org.apache.commons.validator.EmailValidator;

public class EmailUtils {
    public static boolean isValidEmailAddress(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
