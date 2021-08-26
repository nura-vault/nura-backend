package me.micartey.nura.responses;

import lombok.Data;

import java.util.UUID;

@Data
public class SigninResponse implements Response {
    private final String username;
    private final UUID   token;
}
