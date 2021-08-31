package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.TokenController;
import me.micartey.nura.bodies.AuthBody;
import me.micartey.nura.bodies.VaultBody;
import me.micartey.nura.entities.ArchiveEntity;
import me.micartey.nura.entities.PasswordEntity;
import me.micartey.nura.repositories.ArchiveRepository;
import me.micartey.nura.responses.ErrorResponse;
import me.micartey.nura.responses.Response;
import me.micartey.nura.responses.VaultResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/archive")
public class ArchiveController {

    private final TokenController tokenController;

    private final ArchiveRepository archiveRepository;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Response> getArchive(@RequestBody AuthBody body, @Value("${nura.vault.invalidToken}") String invalidToken) {

        if (!tokenController.validTokenMatch(body.getMail(), body.getToken()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidToken));

        val entity = this.getArchiveEntity(body.getMail());

        return ResponseEntity.accepted().body(new VaultResponse(entity.getPasswords()));
    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity<Response> addPassword(@RequestBody VaultBody body, @Value("${nura.vault.invalidToken}") String invalidToken) {

        if (!tokenController.validTokenMatch(body.getMail(), body.getToken()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidToken));

        val entity = this.getArchiveEntity(body.getMail());

        entity.getPasswords().add(new PasswordEntity(
                body.getIdentifier(),
                body.getPassword()
        ));

        archiveRepository.save(entity);

        return ResponseEntity.accepted().body(new VaultResponse(entity.getPasswords()));
    }

    @CrossOrigin
    @DeleteMapping
    public ResponseEntity<Response> removePassword(@RequestBody VaultBody body, @Value("${nura.vault.invalidToken}") String invalidToken) {

        if (!tokenController.validTokenMatch(body.getMail(), body.getToken()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidToken));

        val entity = this.getArchiveEntity(body.getMail());

        val passwordEntity = entity.getPasswords().stream().filter(var -> {
            return var.getIdentifier().equals(body.getIdentifier()) && var.getPassword().equals(body.getPassword());
        }).findFirst().orElse(null);

        entity.getPasswords().remove(passwordEntity);
        archiveRepository.save(entity);

        return ResponseEntity.accepted().body(new VaultResponse(entity.getPasswords()));
    }

    private ArchiveEntity getArchiveEntity(String mail) {
        val entity = archiveRepository.findByMail(mail);
        return entity == null ? new ArchiveEntity(mail) : entity;
    }
}
