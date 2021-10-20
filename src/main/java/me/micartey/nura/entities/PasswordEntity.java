package me.micartey.nura.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class PasswordEntity implements Serializable {
    private final String identifier;
    private final String website;
    private final String username;
    private final String password;
}
