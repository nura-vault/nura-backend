package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.TokenController;
import me.micartey.nura.entities.UserEntity;
import me.micartey.nura.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/signup")
public class SignupController {

    private final TokenController tokenController;

    private final UserRepository userRepository;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<String> onSignup(@RequestBody Map<String, String> body, @Value("${nura.signup.alreayInUse}") String alreadyInUse) {
        val exists = userRepository.existsByMail(
                body.get("mail")
        );

        if (exists) {
            return new ResponseEntity<>(
                    alreadyInUse,
                    HttpStatus.UNAUTHORIZED
            );
        }

        val userEntity = new UserEntity(
                body.get("username"),
                body.get("mail"),
                body.get("password")
        );

        userRepository.save(userEntity);

        return new ResponseEntity<>(
                String.valueOf(tokenController.generateToken(
                        body.get("mail")
                )),
                HttpStatus.ACCEPTED
        );
    }

}
