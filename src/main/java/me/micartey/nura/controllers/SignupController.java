package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.MailVerifier;
import me.micartey.nura.authentication.TokenController;
import me.micartey.nura.bodies.SignupBody;
import me.micartey.nura.entities.UserEntity;
import me.micartey.nura.repositories.UserRepository;
import me.micartey.nura.responses.ErrorResponse;
import me.micartey.nura.responses.Response;
import me.micartey.nura.responses.SigninResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/signup")
public class SignupController {

    private final TokenController tokenController;
    private final MailVerifier    mailVerifier;

    private final UserRepository userRepository;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Response> onSignup(@RequestBody SignupBody body, @Value("${nura.signup.alreadyInUse}") String alreadyInUse, @Value("${nura.invalidMail}") String invalidMail) {

        if (!mailVerifier.isValidMail(body.getMail()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidMail));

        if (userRepository.existsByMail(body.getMail()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(alreadyInUse));

        val entity = new UserEntity(
                body.getUsername(),
                body.getMail(),
                body.getPassword()
        );

        userRepository.save(entity);

        val token = this.tokenController.generateToken(entity.getMail());
        return ResponseEntity.accepted().body(new SigninResponse(entity.getUsername(), token));
    }

}
