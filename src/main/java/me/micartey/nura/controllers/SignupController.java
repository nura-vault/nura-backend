package me.micartey.nura.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.AuditHandler;
import me.micartey.nura.authentication.MailVerifier;
import me.micartey.nura.authentication.TokenHandler;
import me.micartey.nura.bodies.SignupBody;
import me.micartey.nura.entities.AuditLog;
import me.micartey.nura.entities.UserEntity;
import me.micartey.nura.repositories.UserRepository;
import me.micartey.nura.responses.MessageResponse;
import me.micartey.nura.responses.Response;
import me.micartey.nura.responses.SigninResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/api/signup")
public class SignupController {

    private final TokenHandler tokenHandler;
    private final AuditHandler auditHandler;
    private final MailVerifier mailVerifier;

    private final UserRepository  userRepository;

    @CrossOrigin
    @PutMapping
    public ResponseEntity<Response> onSignup(@RequestBody SignupBody body, @RequestHeader("User-Agent") String userAgent, @Value("${nura.signup.alreadyInUse}") String alreadyInUse, @Value("${nura.invalidMail}") String invalidMail) {

        if (!mailVerifier.isValidMail(body.getMail()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(invalidMail));

        if (userRepository.existsByMail(body.getMail()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(alreadyInUse));

        val entity = new UserEntity(
                body.getUsername(),
                body.getMail(),
                body.getPassword()
        );

        userRepository.save(entity);

        val token = this.tokenHandler.generateToken(entity.getMail());
        
        this.auditHandler.createLog(
            AuditLog.Action.LOGIN,
            entity.getMail(),
            token,
            userAgent,
            "Login with new device"
        );
        
        return ResponseEntity.accepted().body(new SigninResponse(entity.getUsername(), token));
    }
}
