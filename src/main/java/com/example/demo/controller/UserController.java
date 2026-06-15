package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

   @GetMapping("/current-user")
    public Map<String, String> currentUser(Authentication authentication) {

        return Map.of(
            "username", authentication.getName()
        );
    }
}