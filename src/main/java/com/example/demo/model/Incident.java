package com.example.demo.model;

import jakarta.persistence.*;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @Column(nullable = false)
    private String number;

    public Incident() {}

    public Incident(String number) {
        this.number = number;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}