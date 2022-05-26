package me.micartey.nura.authentication;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.val;
import me.micartey.nura.entities.AuditEntity;
import me.micartey.nura.entities.AuditLog;
import me.micartey.nura.repositories.AuditRepository;

@Component
@RequiredArgsConstructor
public class AuditHandler {

    private final TokenHandler tokenHandler;

    private final AuditRepository auditRepository;

    public void createLog(AuditLog.Action action, String mail, UUID token, String agent, String message) {
        val audit = this.getAuditEntity(mail);

        audit.getLogs().add(new AuditLog(
                action,
                token,
                agent,
                message,
                System.currentTimeMillis()
        ));

        this.auditRepository.save(audit);
    }

    public List<AuditLog> getLogs(String mail) {
        return this.getAuditEntity(mail).getLogs();
    }

    private AuditEntity getAuditEntity(String mail) {
        val audit = this.auditRepository.findByMail(mail);
        return audit == null ? new AuditEntity(mail) : audit;
    }
}
