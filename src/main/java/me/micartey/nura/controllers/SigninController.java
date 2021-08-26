package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.TokenController;
import me.micartey.nura.bodies.SigninBody;
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

    private final UserRepository userRepository;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Response> onSignin(@RequestBody SigninBody body, @Value("${nura.signin.mismatch}") String mismatch) {
        val match = userRepository.findByMailAndPassword(body.getMail(), body.getPassword());

        if (!match.isPresent())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(mismatch));

        val entity = match.get();
        val token = this.tokenController.generateToken(entity.getMail());
        return ResponseEntity.accepted().body(new SigninResponse(entity.getUsername(), token));
    }

}
