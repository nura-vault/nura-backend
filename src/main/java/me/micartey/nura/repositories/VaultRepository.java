package me.micartey.nura.repositories;

import me.micartey.nura.entities.VaultEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VaultRepository extends MongoRepository<VaultEntity, String> {

    VaultEntity findByMail(String mail);

}
