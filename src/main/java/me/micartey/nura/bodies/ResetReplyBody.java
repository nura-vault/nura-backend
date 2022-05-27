package me.micartey.nura.bodies;

import lombok.Data;

@Data
public class ResetReplyBody {
    private final String token;
    private final String mail;
    private final String password;
}
