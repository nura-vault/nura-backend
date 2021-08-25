package me.micartey.nura.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(
        collection = "users"
)
@Data
public class UserEntity {

    @Id
    private String id;
    private String username, mail, password;

    public UserEntity(String username, String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }
}
