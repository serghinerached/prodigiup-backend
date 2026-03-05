package com.example.demo.controller; // <-- CETTE LIGNE EST CRUCIALE

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class controller {
    
    @GetMapping("/")
    public String sayHello() {
        return "Bonjour depuis le Backend Java de Rached !"; // message affiché 
    }
}