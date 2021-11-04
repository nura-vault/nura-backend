package me.micartey.nura.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import me.micartey.nura.entities.ArchiveEntity;

public interface ArchiveRepository extends MongoRepository<ArchiveEntity, String> {

    ArchiveEntity findByMail(String mail);

}
