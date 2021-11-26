package me.micartey.nura.entities;

import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuditLog {

    private final Action action;

    private final UUID accessToken;
    
    private final String agent;
    private final String message;

    private final long timestamp;

    public enum Action {
        LOGIN, CREATE, ARCHIVE, UNARCHIVE, DELETE, COPY
    }
}
