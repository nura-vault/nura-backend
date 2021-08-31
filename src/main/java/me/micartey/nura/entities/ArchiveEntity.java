package me.micartey.nura.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(
        collection = "archive"
)
@Data
public class ArchiveEntity {

    @Id
    private String id;

    private String                    mail;
    private ArrayList<PasswordEntity> passwords;

    public ArchiveEntity(String mail) {
        this.passwords = new ArrayList<>();
        this.mail = mail;
    }
}
