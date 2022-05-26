package me.micartey.nura.authentication;

import lombok.val;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ResetHandler {

    private final Map<String, List<UUID>> tokens = new HashMap<>();

    public UUID generateResetToken(String mail) {
        val token = UUID.randomUUID();
        val uuids = tokens.getOrDefault(mail, new ArrayList<>());
        uuids.add(token);
        this.tokens.put(mail, uuids);
        return token;
    }

    public boolean revokeResetToken(String mail, UUID token) {
        return this.tokens.getOrDefault(mail, Collections.emptyList()).remove(token);
    }

    public void revokeResetTokens(String mail) {
        this.tokens.remove(mail);
    }

    public boolean validResetToken(String mail, UUID token) {
        return this.tokens.getOrDefault(mail, Collections.emptyList()).contains(token);
    }
}
