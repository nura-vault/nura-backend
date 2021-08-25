package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.TokenController;
import me.micartey.nura.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/signin")
public class SigninController {

    private final TokenController tokenController;

    private final UserRepository userRepository;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<String> onSignin(@RequestBody Map<String, String> body, @Value("${nura.signin.mismatch}") String mismatch) {
        val exists = userRepository.existsByMailAndPassword(
                body.get("mail"),
                body.get("password")
        );

        if (!exists) {
            return new ResponseEntity<>(
                    mismatch,
                    HttpStatus.UNAUTHORIZED
            );
        }

        return new ResponseEntity<>(
                String.valueOf(tokenController.generateToken(
                        body.get("mail")
                )),
                HttpStatus.ACCEPTED
        );
    }

}
