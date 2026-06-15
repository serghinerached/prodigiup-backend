package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/userprofile")
    public Map<String, String> getUserProfile() {

        return Map.of(
            "userProfile", String.valueOf(System.getenv("USERPROFILE")),
            "userName", String.valueOf(System.getenv("USERNAME"))
        );
    }
}