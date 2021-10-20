package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.TokenController;
import me.micartey.nura.bodies.VaultBody;
import me.micartey.nura.entities.PasswordEntity;
import me.micartey.nura.entities.VaultEntity;
import me.micartey.nura.repositories.VaultRepository;
import me.micartey.nura.responses.ErrorResponse;
import me.micartey.nura.responses.Response;
import me.micartey.nura.responses.VaultResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vault")
public class VaultController {

    private final TokenController tokenController;

    private final VaultRepository vaultRepository;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Response> getVault(@RequestHeader("Authorization") String auth, @Value("${nura.vault.invalidToken}") String invalidToken) {
        String decoded = new String(Base64.getDecoder().decode(auth));
        String mail = decoded.split(":")[0];
        UUID token = UUID.fromString(decoded.split(":")[1]);

        if (!tokenController.validTokenMatch(mail, token))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidToken));

        val entity = this.getVaultEntity(mail);

        return ResponseEntity.accepted().body(new VaultResponse(entity.getPasswords()));
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Response> addPassword(@RequestHeader("Authorization") String auth, @RequestBody VaultBody body, @Value("${nura.vault.invalidToken}") String invalidToken) {
        String decoded = new String(Base64.getDecoder().decode(auth));
        String mail = decoded.split(":")[0];
        UUID token = UUID.fromString(decoded.split(":")[1]);

        if (!tokenController.validTokenMatch(mail, token))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidToken));

        val entity = this.getVaultEntity(mail);

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
    public ResponseEntity<Response> removePassword(@RequestHeader("Authorization") String auth, @RequestBody VaultBody body, @Value("${nura.vault.invalidToken}") String invalidToken) {
        String decoded = new String(Base64.getDecoder().decode(auth));
        String mail = decoded.split(":")[0];
        UUID token = UUID.fromString(decoded.split(":")[1]);

        if (!tokenController.validTokenMatch(mail, token))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidToken));

        val entity = this.getVaultEntity(mail);

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
