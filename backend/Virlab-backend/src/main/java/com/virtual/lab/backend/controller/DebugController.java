package com.virtual.lab.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/api/debug/principal")
    public ResponseEntity<?> debugPrincipal(Authentication auth) {
        Object principal = auth.getPrincipal();

        Map<String, Object> debug = new HashMap<>();
        debug.put("principalClass", principal.getClass().getName());
        debug.put("principalString", principal.toString());
        debug.put("isUserPrincipal", principal instanceof UserPrincipal);
        debug.put("isSpringUser", principal instanceof org.springframework.security.core.userdetails.User);

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User springUser =
                    (org.springframework.security.core.userdetails.User) principal;
            debug.put("username", springUser.getUsername());
            debug.put("authorities", springUser.getAuthorities());
        }

        return ResponseEntity.ok(debug);
    }
}