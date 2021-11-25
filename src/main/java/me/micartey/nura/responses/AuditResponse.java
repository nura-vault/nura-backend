package me.micartey.nura.responses;

import lombok.Data;
import me.micartey.nura.entities.AuditLog;

import java.util.List;

@Data
public class AuditResponse implements Response {
    private final List<AuditLog> audit;
}
