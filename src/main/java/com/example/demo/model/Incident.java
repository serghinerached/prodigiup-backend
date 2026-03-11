package com.example.demo.model;

import jakarta.persistence.*;

import java.util.Date;

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
    private Date opened;
    @Column(name="assigned_to")
    private String assignedTo;
    private String state;
    @Column(name="assignement_group")
    private String assignementGroup;
    @Column(name="requested_for")
    private String requestedfor;
    private Date resolved;
    private Date closed;
    private String service;
    @Column(name="reopen_count")
    private Integer reopencount;


    public Incident() {}

    public Incident(String number, Date opened, String assignedTo, String state, String assignementGroup, String requestedfor, 
        Date resolved, Date closed, String service, Integer reopencount) {
        this.number = number;
        this.opened = opened;
        this.assignedTo = assignedTo;
        this.state = state;
        this.assignementGroup = assignementGroup;
        this.requestedfor = requestedfor;
        this.resolved = resolved;
        this.closed = closed;       
        this.service = service;
        this.reopencount = reopencount;
    }
  

    // getters--------
    @Nullable
    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

     public Date getOpened() {
        return opened;
    }

     public String getAssignedTo() {
        return assignedTo;
    }
    public String getState() {
        return state;
    }

    public String getAssignementGroup() {
        return assignementGroup;
    }

    public String getRequestedfor() {
        return requestedfor;
    }   
    public Date getResolved() {
        return resolved;
    }
    public Date getClosed() {
        return closed;
    }

    public String getService() {
        return service;
    }
    public Integer getReopencount() {
        return reopencount;
    }

    // setters-----------------------

    public void setNumber(String number) {
        this.number = number;
    }

     public void setOpened(Date opened) {
        this.opened = opened;
    }

     public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setAssignementGroup(String assignementGroup) {
        this.assignementGroup = assignementGroup;
    }
    public void setRequestedfor(String requestedfor) {
        this.requestedfor = requestedfor;
    }
    public void setResolved(Date resolved) {
        this.resolved = resolved;
    }
    public void setClosed(Date closed) {
        this.closed = closed;
    }
    public void setService(String service) {
        this.service = service;
    }
    public void setReopencount(Integer reopencount) {
        this.reopencount = reopencount;
    }
}