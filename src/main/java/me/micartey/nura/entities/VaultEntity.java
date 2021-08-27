package me.micartey.nura.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(
        collection = "vault"
)
@Data
public class VaultEntity {

    @Id
    private String id;

    private String                    mail;
    private ArrayList<PasswordEntity> passwords;

    public VaultEntity(String mail) {
        this.passwords = new ArrayList<>();
        this.mail = mail;
    }
}
