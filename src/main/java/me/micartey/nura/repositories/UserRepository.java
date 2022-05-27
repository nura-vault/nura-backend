package me.micartey.nura.repositories;

import me.micartey.nura.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    UserEntity findByMail(String mail);

    boolean existsByMail(String mail);

    Optional<UserEntity> findByMailAndPassword(String mail, String password);

}
