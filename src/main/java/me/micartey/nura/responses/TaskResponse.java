package me.micartey.nura.responses;

import org.apache.catalina.connector.Response;

import lombok.Data;

@Data
public class TaskResponse extends Response {
    private final int id;
}
