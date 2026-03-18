package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.lang.Nullable;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    private LocalDateTime opened;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(nullable = false)
    private String state;

    @Column(name = "assignment_group", nullable = false)
    private String assignmentGroup;

    @Column(name = "requested_for", nullable = false)
    private String requestedFor;

    private LocalDateTime resolved;
    private LocalDateTime closed;

    private String service;

    @Column(name = "reopen_count")
    private Integer reopenCount;

    public Incident() {}

    public Incident(String number, LocalDateTime opened, String assignedTo, String state, String assignmentGroup, String requestedFor, 
        LocalDateTime resolved, LocalDateTime closed, String service, Integer reopenCount) {
        this.number = number;
        this.opened = opened;
        this.assignedTo = assignedTo;
        this.state = state;
        this.assignmentGroup = assignmentGroup;
        this.requestedFor = requestedFor;
        this.resolved = resolved;
        this.closed = closed;       
        this.service = service;
        this.reopenCount = reopenCount;
    }
  

    // getters--------
    @Nullable
    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

     public LocalDateTime getOpened() {
        return opened;
    }

     public String getAssignedTo() {
        return assignedTo;
    }
    public String getState() {
        return state;
    }

    public String getAssignmentGroup() {
        return assignmentGroup;
    }

    public String getRequestedfor() {
        return requestedFor;
    }   
    public LocalDateTime getResolved() {
        return resolved;
    }
    public LocalDateTime getClosed() {
        return closed;
    }

    public String getService() {
        return service;
    }
    public Integer getReopencount() {
        return reopenCount;
    }

    // setters-----------------------

    public void setNumber(String number) {
        this.number = number;
    }

     public void setOpened(LocalDateTime opened) {
        this.opened = opened;
    }

     public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setAssignmentGroup(String assignmentGroup) {
        this.assignmentGroup = assignmentGroup;
    }
    public void setRequestedFor(String requestedFor) {
        this.requestedFor = requestedFor;
    }
    public void setResolved(LocalDateTime resolved) {
        this.resolved = resolved;
    }
    public void setClosed(LocalDateTime closed) {
        this.closed = closed;
    }
    public void setService(String service) {
        this.service = service;
    }
    public void setReopenCount(Integer reopenCount) {
        this.reopenCount = reopenCount;
    }
}