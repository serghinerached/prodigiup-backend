package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "performance1")
public class Performance1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String number;
    private String service;
    private LocalDateTime opened;
    private LocalDateTime resolved;
    private Integer mttr8days;

    public Performance1() {}

    public Performance1(String number,String service,LocalDateTime opened,LocalDateTime resolved, Integer mttr8days) {
        this.number = number;
        this.opened = opened;
        this.resolved = resolved;
        this.service = service;
        this.mttr8days = mttr8days;
    }
  

    // getters--------
    @Nullable
    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

     public String getService() {
        return service;
    }

     public LocalDateTime getOpened() {
        return opened;
    }

    
    public LocalDateTime getResolved() {
        return resolved;
    }
   
    public Integer getMttr8days() {
        return mttr8days;
    }

    // setters-----------------------

    public void setNumber(String number) {
        this.number = number;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setOpened(LocalDateTime opened) {
        this.opened = opened;
    }

    public void setResolved(LocalDateTime resolved) {
        this.resolved = resolved;
    }
  
    public void setMttr8days(Integer mttr8days) {
        this.mttr8days = mttr8days;
    }
}