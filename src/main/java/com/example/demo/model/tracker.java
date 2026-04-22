package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "tracker") 

public class tracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "week")
    private Integer week;

    @Column(name = "opened")
    private LocalDateTime opened;

    @Column(name = "number")
    private String number;

    @Column(name = "type")
    private String type;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "state")
    private String state;

    @Column(name = "assignment_group")
    private String assignmentGroup;

    @Column(name = "requested_for")
    private String requestedFor;

    @Column(name = "resolved")
    private LocalDateTime resolved;

    @Column(name = "closed")
    private LocalDateTime closed;

    @Column(name = "service")
    private String service;

    @Column(name = "reopen_count")
    private Integer reopenCount;

    @Column(name = "mttr")
    private double mttr;


    public tracker() {}

    public tracker(Integer week, LocalDateTime opened, String number,String type, String assignedTo, String state, String assignmentGroup, String requestedFor, 
        LocalDateTime resolved, LocalDateTime closed, String service, Integer reopenCount, double mttr) {
        this.week = week;
        this.opened = opened;
        this.number = number;
        this.type = type;
        this.assignedTo = assignedTo;
        this.state = state;
        this.assignmentGroup = assignmentGroup;
        this.requestedFor = requestedFor;
        this.resolved = resolved;
        this.closed = closed;       
        this.service = service;
        this.reopenCount = reopenCount;
        this.mttr = mttr;
    }
  

    // getters--------
    @Nullable
    public Long getId() {
        return id;
    }

    public Integer getWeek() {
        return week;
    }

    public LocalDateTime getOpened() {
        return opened;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
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

    public String getRequestedFor() {
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

    public double getMttr() {
        return mttr;
    }

    // setters-----------------------

    public void setWeek(Integer week) {
        this.week = week;
    }

    public void setOpened(LocalDateTime opened) {
        this.opened = opened;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
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
    public void setMttr(double mttr) {
        this.mttr = mttr;
    }
}