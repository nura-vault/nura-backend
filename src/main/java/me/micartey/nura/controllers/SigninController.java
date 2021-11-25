package me.micartey.nura.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.AuditHandler;
import me.micartey.nura.authentication.AuthConverter;
import me.micartey.nura.authentication.MailVerifier;
import me.micartey.nura.authentication.TokenHandler;
import me.micartey.nura.entities.AuditLog;
import me.micartey.nura.repositories.UserRepository;
import me.micartey.nura.responses.MessageResponse;
import me.micartey.nura.responses.Response;
import me.micartey.nura.responses.SigninResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/api/signin")
public class SigninController {

    private final TokenHandler tokenHandler;
    private final AuditHandler auditHandler;
    private final MailVerifier mailVerifier;

    private final UserRepository  userRepository;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Response> onSignin(@RequestHeader("Authorization") AuthConverter.Auth auth, @RequestHeader("User-Agent") String userAgent, @Value("${nura.signin.mismatch}") String mismatch, @Value("${nura.invalidMail}") String invalidMail) {

        if (!mailVerifier.isValidMail(auth.getKey()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(invalidMail));

        val entity = userRepository.findByMailAndPassword(
                auth.getKey(),
                auth.getValue()
        );

        if (!entity.isPresent())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(mismatch));

        val token = this.tokenHandler.generateToken(entity.get().getMail());
        
        this.auditHandler.createLog(
            AuditLog.Action.LOGIN,
            entity.get().getMail(),
            token,
            userAgent,
            "Login with new device"
        );

        return ResponseEntity.accepted().body(new SigninResponse(entity.get().getUsername(), token));
    }

}
