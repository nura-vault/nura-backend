package me.micartey.nura.controllers;

import lombok.AllArgsConstructor;
import lombok.val;
import me.micartey.nura.authentication.AuditHandler;
import me.micartey.nura.authentication.MailVerifier;
import me.micartey.nura.authentication.ResetHandler;
import me.micartey.nura.bodies.SignupBody;
import me.micartey.nura.entities.AuditLog;
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
    public ResponseEntity<Response> resetPassword(@RequestBody SignupBody body, @RequestHeader("User-Agent") String userAgent, @Value("${nura.invalidMail}") String invalidMail, @Value("${nura.host}") String host) {

        if (!mailVerifier.isValidMail(body.getMail()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(invalidMail));

        this.auditHandler.createLog(
                AuditLog.Action.RESET,
                body.getMail(),
                UUID.randomUUID(),
                userAgent,
                "Password reset requested"
        );

        if (userRepository.existsByMail(body.getMail()))
            return ResponseEntity.accepted().body(new MessageResponse("Reset mail send!"));

        val mailBody = new MailRequests(
            "mail.micartey.dev",
            25,
            false,
            new MailRequests.Auth(
                    "noreply@micartey.dev",
                    "Qq?2qi3+!NE?&+f&"
            ),
            new MailRequests.Message(
                    "me@micartey.dev",
                    "Reset Password",
                    PASSWORD_RESET_HTML.replace("$LINK$", host + "/reset?token=" + resetHandler.generateResetToken(body.getMail()))
            )
        );

        val template = new RestTemplate();
        template.postForLocation("http://localhost:9004/api/v1/mail/send", mailBody);

        return ResponseEntity.accepted().body(new MessageResponse("Reset mail send!"));
    }
}
