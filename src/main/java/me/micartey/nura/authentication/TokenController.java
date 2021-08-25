package me.micartey.nura.authentication;

import lombok.val;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenController {

    private final Map<String, List<UUID>> tokens = new HashMap<>();

    public UUID generateToken(String mail) {
        val token = UUID.randomUUID();
        val uuids = tokens.getOrDefault(mail, new ArrayList<>());
        uuids.add(token);
        tokens.put(mail, uuids);
        return token;
    }

    public boolean validTokenMatch(String mail, UUID token) {
        return tokens.getOrDefault(mail, Collections.emptyList()).contains(token);
    }
}
