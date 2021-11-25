package me.micartey.nura.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import me.micartey.nura.authentication.AuditHandler;
import me.micartey.nura.authentication.AuthConverter;
import me.micartey.nura.authentication.TokenHandler;
import me.micartey.nura.responses.Response;

@RestController
@AllArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    
    private final TokenHandler tokenHandler;
    private final AuditHandler auditHandler;
    
    @GetMapping
    @RequestMapping("/{id}")
    public ResponseEntity<Response> computeTask(@RequestParam String id, @RequestHeader("User-Agent") String userAgent) {
        return null;
    }

    @GetMapping
    @RequestMapping("/create/{type}")
    public ResponseEntity<Response> createTask(@RequestParam String type, @RequestHeader("Authorization") Optional<AuthConverter.Auth> auth) {
        return null;
    }

}
