package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Date;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "performance1")
public class Performance1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @Column(nullable = false)
    private String number;
    private String service;
    private Date opened;
    private Date resolved;
    private int mttr8days;


    public Performance1() {}

    public Performance1(String number, Date opened, Date resolved, int mttr8days, String service) {
         this.service = service;
        this.number = number;
        this.service = service;
        this.opened = opened;
        this.resolved = resolved;
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
     public Date getOpened() {
        return opened;
    }

    public Date getResolved() {
        return resolved;
    }
    public int getMttr8days() {
        return mttr8days;
    }
 
    // setters-----------------------

    public void setNumber(String number) {
        this.number = number;
    }

    public void setService(String service) {
        this.service = service; 
    }
     public void setOpened(Date opened) {
        this.opened = opened;
    }
    public void setResolved(Date resolved) {
        this.resolved = resolved;
    }
    public void setMttr8days(int mttr8days) {
        this.mttr8days = mttr8days;
    }
}