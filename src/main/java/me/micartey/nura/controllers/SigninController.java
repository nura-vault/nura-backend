package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.MailVerifier;
import me.micartey.nura.authentication.TokenController;
import me.micartey.nura.authentication.AuthConverter;
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
@RequestMapping("/api/signin")
public class SigninController {

    private final TokenController tokenController;
    private final MailVerifier    mailVerifier;

    private final UserRepository userRepository;

    //https://stackoverflow.com/questions/32271042/how-to-convert-requestheader-to-custom-object-in-spring

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Response> onSignin(@RequestHeader("Authorization") AuthConverter.Auth auth, @Value("${nura.signin.mismatch}") String mismatch, @Value("${nura.invalidMail}") String invalidMail) {

        if (!mailVerifier.isValidMail(auth.getKey()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidMail));

        val match = userRepository.findByMailAndPassword(
                auth.getKey(),
                auth.getValue()
        );

        if (!match.isPresent())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(mismatch));

        val entity = match.get();
        val token = this.tokenController.generateToken(entity.getMail());
        return ResponseEntity.accepted().body(new SigninResponse(entity.getUsername(), token));
    }

}
