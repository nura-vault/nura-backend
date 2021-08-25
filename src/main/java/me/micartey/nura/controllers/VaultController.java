package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.var;
import me.micartey.nura.authentication.TokenController;
import me.micartey.nura.entities.VaultEntity;
import me.micartey.nura.repositories.VaultRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vault")
public class VaultController {

    private final TokenController tokenController;

    private final VaultRepository vaultRepository;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<String> getVault(@RequestBody Map<String, String> body, @Value("${nura.vault.invalidToken}") String invalidToken) {
        if (!this.isAuthenticated(body)) {
            return new ResponseEntity<>(
                    invalidToken,
                    HttpStatus.UNAUTHORIZED
            );
        }

        val entity = this.getVaultEntity(
                body.get("mail")
        );

        return new ResponseEntity<>(
                entity.getPasswords().toString(),
                HttpStatus.ACCEPTED
        );
    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity<String> addPassword(@RequestBody Map<String, String> body, @Value("${nura.vault.invalidToken}") String invalidToken) {
        if (!this.isAuthenticated(body)) {
            return new ResponseEntity<>(
                    invalidToken,
                    HttpStatus.UNAUTHORIZED
            );
        }

        val entity = this.getVaultEntity(
                body.get("mail")
        );

        entity.getPasswords().add(new AbstractMap.SimpleEntry<>(
                body.get("name"),
                body.get("password")
        ));

        vaultRepository.save(entity);

        return this.getVault(body, invalidToken);
    }

    private VaultEntity getVaultEntity(String mail) {
        val entity = vaultRepository.findByMail(mail);
        return entity == null ? new VaultEntity(mail) : entity;
    }

    private boolean isAuthenticated(Map<String, String> body) {
        return tokenController.validTokenMatch(
                body.get("mail"),
                UUID.fromString(
                        body.get("token")
                )
        );
    }
}
