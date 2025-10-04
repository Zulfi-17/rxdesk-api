package com.example.rxdesk.model;

import java.time.Instant;

public class Ticket {
    public int id;
    public String title;
    public String description;
    public String createdBy;
    public String assignedTo = "";
    public Status status = Status.OPEN;
    public Priority priority = Priority.MEDIUM;
    public long createdOn = Instant.now().getEpochSecond();
    public long closedOn = 0;

    public Ticket() {} // for JSON
    public Ticket(int id, String title, String description, String createdBy){
        this.id = id; this.title = title; this.description = description; this.createdBy = createdBy;
    }
}

