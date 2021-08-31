package me.micartey.nura.repositories;

import me.micartey.nura.entities.ArchiveEntity;
import me.micartey.nura.entities.VaultEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArchiveRepository extends MongoRepository<ArchiveEntity, String> {

    ArchiveEntity findByMail(String mail);

}
