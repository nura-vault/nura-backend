package me.micartey.nura.entities;

import java.util.LinkedList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(
        collection = "audit"
)
@Data
public class AuditEntity {
    
    @Id
    private String id;

    private String mail;
    private LinkedList<AuditLog> logs;

    public AuditEntity(String mail) {
        this.logs = new LinkedList<>();
        this.mail = mail;
    }
}
