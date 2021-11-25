package me.micartey.nura.authentication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public class TaskHandler {
    
    private final Map<String, Task> taskList = new HashMap<>();

    @Data
    public static class Task {
        private final TaskType type;
        private final String id, payload;
    }

    public enum TaskType {
        RESET_PASSWORD, REVOKE_ACCESS_TOKEN, REVOKE_ACCESS_TOKENS
    }
}
