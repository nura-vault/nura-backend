package me.micartey.nura.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.AuthConverter;
import me.micartey.nura.authentication.TokenHandler;
import me.micartey.nura.bodies.VaultBody;
import me.micartey.nura.entities.PasswordEntity;
import me.micartey.nura.entities.VaultEntity;
import me.micartey.nura.repositories.VaultRepository;
import me.micartey.nura.responses.MessageResponse;
import me.micartey.nura.responses.Response;
import me.micartey.nura.responses.VaultResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vault")
public class VaultController {

    private final TokenHandler tokenHandler;

    private final VaultRepository vaultRepository;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Response> getVault(@RequestHeader("Authorization") AuthConverter.Auth auth, @Value("${nura.vault.invalidToken}") String invalidToken) {

        if (!tokenHandler.validTokenMatch(auth.getKey(), UUID.fromString(auth.getValue())))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(invalidToken));

        val entity = this.getVaultEntity(auth.getKey());

        return ResponseEntity.accepted().body(new VaultResponse(entity.getPasswords()));
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Response> addPassword(@RequestHeader("Authorization") AuthConverter.Auth auth, @RequestBody VaultBody body, @Value("${nura.vault.invalidToken}") String invalidToken) {

        if (!tokenHandler.validTokenMatch(auth.getKey(), UUID.fromString(auth.getValue())))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(invalidToken));

        val entity = this.getVaultEntity(auth.getKey());

        entity.getPasswords().add(new PasswordEntity(
                body.getIdentifier(),
                body.getWebsite(),
                body.getUsername(),
                body.getPassword()
        ));

        vaultRepository.save(entity);

        return ResponseEntity.accepted().body(new VaultResponse(entity.getPasswords()));
    }

    @CrossOrigin
    @DeleteMapping
    public ResponseEntity<Response> removePassword(@RequestHeader("Authorization") AuthConverter.Auth auth, @RequestBody VaultBody body, @Value("${nura.vault.invalidToken}") String invalidToken) {

        if (!tokenHandler.validTokenMatch(auth.getKey(), UUID.fromString(auth.getValue())))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(invalidToken));

        val entity = this.getVaultEntity(auth.getKey());

        val passwordEntity = entity.getPasswords().stream().filter(var -> {
            return var.getIdentifier().equals(body.getIdentifier()) && var.getPassword().equals(body.getPassword());
        }).findFirst().orElse(null);

        entity.getPasswords().remove(passwordEntity);
        vaultRepository.save(entity);

        return ResponseEntity.accepted().body(new VaultResponse(entity.getPasswords()));
    }

    private VaultEntity getVaultEntity(String mail) {
        val entity = vaultRepository.findByMail(mail);
        return entity == null ? new VaultEntity(mail) : entity;
    }
}
