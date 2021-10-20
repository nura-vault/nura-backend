package me.micartey.nura.bodies;

import lombok.Data;

import java.util.UUID;

@Data
public class VaultBody {
    private final String identifier;
    private final String website;
    private final String username;
    private final String password;
}
