package me.micartey.nura.entities;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(
        collection = "vault"
)
@Data
public class VaultEntity {

    @Id
    private String id;

    private final String                    mail;
    private ArrayList<PasswordEntity> passwords;

    public VaultEntity(String mail) {
        this.passwords = new ArrayList<>();
        this.mail = mail;
    }
}
