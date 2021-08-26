package me.micartey.nura.bodies;

import lombok.Data;

@Data
public class SignupBody {
    private final String username;
    private final String mail;
    private final String password;
}
