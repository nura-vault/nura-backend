package me.micartey.nura.responses;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class VaultResponse implements Response {
    private final List<Map.Entry<String, String>> vault;
}
