package me.micartey.nura.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(
        collection = "users"
)
@Data
@RequiredArgsConstructor
public class UserEntity {

    @Id
    private String id;
    private final String username, mail, password;

}
