package com.example.rxdesk.api;

import com.example.rxdesk.model.*;
import com.example.rxdesk.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin
public class TicketController {
    private final TicketService svc;
    public TicketController(TicketService svc){ this.svc = svc; }

    @GetMapping
    public List<Ticket> list(@RequestParam Optional<String> q,
                             @RequestParam Optional<Status> status,
                             @RequestParam Optional<Priority> priority,
                             @RequestParam Optional<Boolean> assignedOnly){
        return svc.list(q, status, priority, assignedOnly);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> get(@PathVariable int id){
        return svc.find(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody CreateTicketRequest body){
        Ticket t = new Ticket(0, body.title, body.description, body.createdBy);
        if (body.priority != null) t.priority = body.priority;
        return ResponseEntity.ok(svc.create(t));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Ticket> start(@PathVariable int id){
        return svc.start(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Ticket> assign(@PathVariable int id, @RequestBody AssignRequest body){
        return svc.assign(id, body.assignee).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<Ticket> close(@PathVariable int id){
        return svc.close(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats")
    public Map<String,Object> stats(){ return svc.stats(); }
}
