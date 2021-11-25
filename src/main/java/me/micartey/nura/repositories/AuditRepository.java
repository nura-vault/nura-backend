package me.micartey.nura.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import me.micartey.nura.entities.AuditEntity;

public interface AuditRepository extends MongoRepository<AuditEntity, String> {
    
    AuditEntity findByMail(String mail);

}
