package me.micartey.nura.repositories;

import me.micartey.nura.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    UserEntity findByMail(String mail);

    boolean existsByMail(String mail);

    boolean existsByMailAndPassword(String mail, String password);

}
