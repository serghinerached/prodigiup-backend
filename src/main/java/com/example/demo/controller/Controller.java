package com.example.demo.controller; // <-- CETTE LIGNE EST CRUCIALE

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "*")
public class Controller {
    
    @GetMapping("/api/hello") // message d'accueil de connexion au backend.
    public String sayHello() {
        return "Connexion Render/Backend/Java = OK !"; // message affiché 
    }
}