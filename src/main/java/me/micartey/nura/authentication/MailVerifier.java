package me.micartey.nura.authentication;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class MailVerifier {

    public boolean isValidMail(String mail) {
        return Pattern.matches("^([\\p{L}-_\\.]+){1,64}@([\\p{L}-_\\.]+){2,255}.[a-z]{2,}$", mail);
    }

}
