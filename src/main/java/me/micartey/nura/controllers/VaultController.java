package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.TokenController;
import me.micartey.nura.bodies.AuthBody;
import me.micartey.nura.bodies.VaultBody;
import me.micartey.nura.entities.VaultEntity;
import me.micartey.nura.repositories.VaultRepository;
import me.micartey.nura.responses.ErrorResponse;
import me.micartey.nura.responses.Response;
import me.micartey.nura.responses.VaultResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vault")
public class VaultController {

    private final TokenController tokenController;

    private final VaultRepository vaultRepository;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Response> getVault(@RequestBody AuthBody body, @Value("${nura.vault.invalidToken}") String invalidToken) {

        if (!tokenController.validTokenMatch(body.getMail(), body.getToken()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidToken));

        val entity = this.getVaultEntity(body.getMail());

        return ResponseEntity.accepted().body(new VaultResponse(entity.getPasswords()));
    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity<Response> addPassword(@RequestBody VaultBody body, @Value("${nura.vault.invalidToken}") String invalidToken) {

        if (!tokenController.validTokenMatch(body.getMail(), body.getToken()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidToken));

        val entity = this.getVaultEntity(body.getMail());

        entity.getPasswords().add(new AbstractMap.SimpleEntry<>(
                body.getName(),
                body.getPassword()
        ));

        vaultRepository.save(entity);

        return ResponseEntity.accepted().body(new VaultResponse(entity.getPasswords()));
    }

    private VaultEntity getVaultEntity(String mail) {
        val entity = vaultRepository.findByMail(mail);
        return entity == null ? new VaultEntity(mail) : entity;
    }
}
