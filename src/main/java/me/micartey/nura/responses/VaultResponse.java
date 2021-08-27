package me.micartey.nura.responses;

import lombok.Data;
import me.micartey.nura.entities.PasswordEntity;

import java.util.List;

@Data
public class VaultResponse implements Response {
    private final List<PasswordEntity> vault;
}
