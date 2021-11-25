package me.micartey.nura.responses;

import lombok.Data;

@Data
public class MessageResponse implements Response {
    private final String message;
}
