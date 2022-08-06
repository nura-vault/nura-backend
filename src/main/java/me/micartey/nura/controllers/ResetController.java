package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.AuditHandler;
import me.micartey.nura.authentication.MailVerifier;
import me.micartey.nura.authentication.ResetHandler;
import me.micartey.nura.bodies.ResetReplyBody;
import me.micartey.nura.bodies.ResetRequestBody;
import me.micartey.nura.entities.AuditLog;
import me.micartey.nura.entities.UserEntity;
import me.micartey.nura.repositories.UserRepository;
import me.micartey.nura.requests.MailRequests;
import me.micartey.nura.responses.MessageResponse;
import me.micartey.nura.responses.Response;
import me.micartey.nura.utilities.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/api/reset")
public class ResetController {

    private final String PASSWORD_RESET_HTML = Stream.getContent(getClass().getResourceAsStream("/resetpw.html"));

    private final UserRepository userRepository;
    private final AuditHandler   auditHandler;
    private final ResetHandler   resetHandler;
    private final MailVerifier   mailVerifier;

    @CrossOrigin
    @PutMapping("/password")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetRequestBody body, @RequestHeader("User-Agent") String userAgent, @Value("${nura.invalidMail}") String invalidMail, @Value("${nura.host.frontend}") String host, @Value("${nura.host.mail}") String blast) {

        if (!mailVerifier.isValidMail(body.getMail()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(invalidMail));

        this.auditHandler.createLog(
                AuditLog.Action.RESET,
                body.getMail(),
                UUID.randomUUID(),
                userAgent,
                "Password reset requested"
        );

        if (!userRepository.existsByMail(body.getMail()))
            return ResponseEntity.accepted().body(new MessageResponse("Reset mail send!"));

        val mailBody = new MailRequests(
            System.getenv("BLAST_HOST"),
            Integer.parseInt(System.getenv("BLAST_PORT")),
            Boolean.parseBoolean(System.getenv("BLAST_TLS")),
            new MailRequests.Auth(
                    System.getenv("BLAST_MAIL"),
                    System.getenv("BLAST_PASSWORD")
            ),
            new MailRequests.Message(
                    body.getMail(),
                    "Reset Password",
                    PASSWORD_RESET_HTML
                            .replace("$USER$", userRepository.findByMail(body.getMail()).getUsername())
                            .replace("$LINK$", host + "reset?token=" + resetHandler.generateResetToken(body.getMail()))
            )
        );

        new Thread(() -> {
            val template = new RestTemplate();
            template.postForLocation(blast + "api/v1/mail/send", mailBody);
        }).start();

        return ResponseEntity.accepted().body(new MessageResponse("Reset mail send!"));
    }

    @CrossOrigin
    @PostMapping("/password")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetReplyBody body, @RequestHeader("User-Agent") String userAgent, @Value("${nura.vault.invalidToken}") String invalidToken, @Value("${nura.invalidMail}") String invalidMail) {

        if (!mailVerifier.isValidMail(body.getMail()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(invalidMail));

        if (!resetHandler.validResetToken(body.getMail(), UUID.fromString(body.getToken())))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(invalidToken));

        resetHandler.revokeResetTokens(body.getMail());

        val userEntity = userRepository.findByMail(
                body.getMail()
        );

        val newUserEntity = new UserEntity(
                userEntity.getUsername(),
                userEntity.getMail(),
                body.getPassword()
        );

        userRepository.delete(userEntity);
        userRepository.save(newUserEntity);

        this.auditHandler.createLog(
                AuditLog.Action.RESET,
                body.getMail(),
                UUID.fromString(body.getToken()),
                userAgent,
                "Password has been reset"
        );

        return ResponseEntity.accepted().body(new MessageResponse("Password has been successfully reset"));
    }
}
