package me.micartey.nura.responses;

import lombok.Data;

@Data
public class ErrorResponse implements Response {
    private final String error;
}
