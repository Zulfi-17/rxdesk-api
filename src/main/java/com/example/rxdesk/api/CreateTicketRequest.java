package com.example.rxdesk.api;
import com.example.rxdesk.model.Priority;

public class CreateTicketRequest {
    public String title;
    public String description;
    public String createdBy;
    public Priority priority; // optional
}
