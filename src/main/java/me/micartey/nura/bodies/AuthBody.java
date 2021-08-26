package me.micartey.nura.bodies;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthBody {
    private final String mail;
    private final UUID   token;
}
