package me.micartey.nura.bodies;

import lombok.Data;

import java.util.UUID;

@Data
public class VaultBody {
    private final String mail;
    private final UUID   token;

    private final String identifier;
    private final String password;
}
