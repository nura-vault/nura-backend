package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.MailVerifier;
import me.micartey.nura.authentication.TokenController;
import me.micartey.nura.repositories.UserRepository;
import me.micartey.nura.responses.ErrorResponse;
import me.micartey.nura.responses.Response;
import me.micartey.nura.responses.SigninResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@AllArgsConstructor
@RequestMapping("/api/signin")
public class SigninController {

    private final TokenController tokenController;
    private final MailVerifier    mailVerifier;

    private final UserRepository userRepository;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Response> onSignin(@RequestHeader("Authorization") String auth, @Value("${nura.signin.mismatch}") String mismatch, @Value("${nura.invalidMail}") String invalidMail) {
        String decoded = new String(Base64.getDecoder().decode(auth));
        String mail = decoded.split(":")[0];
        String password = decoded.split(":")[1];

        if (!mailVerifier.isValidMail(mail))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(invalidMail));

        val match = userRepository.findByMailAndPassword(mail, password);

        if (!match.isPresent())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(mismatch));

        val entity = match.get();
        val token = this.tokenController.generateToken(entity.getMail());
        return ResponseEntity.accepted().body(new SigninResponse(entity.getUsername(), token));
    }

}
