package me.micartey.nura.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import me.micartey.nura.authentication.AuditHandler;
import me.micartey.nura.authentication.AuthConverter;
import me.micartey.nura.authentication.TokenHandler;
import me.micartey.nura.responses.AuditResponse;
import me.micartey.nura.responses.MessageResponse;
import me.micartey.nura.responses.Response;

@RestController
@AllArgsConstructor
@RequestMapping("/api/audit")
public class AuditController {

    private final TokenHandler tokenController;
    private final AuditHandler  auditRegulator;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Response> getAudit(@RequestHeader("Authorization") AuthConverter.Auth auth, @Value("${nura.vault.invalidToken}") String invalidToken) {

        if (!tokenController.validTokenMatch(auth.getKey(), UUID.fromString(auth.getValue())))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(invalidToken));
        
        return ResponseEntity.accepted().body(new AuditResponse(this.auditRegulator.getLogs(auth.getKey())));
    }

}
