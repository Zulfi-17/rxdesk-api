package com.example.rxdesk.service;

import com.example.rxdesk.model.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final Map<Integer, Ticket> store = new ConcurrentHashMap<>();
    private int nextId = 1;

    public List<Ticket> list(Optional<String> q, Optional<Status> status,
                             Optional<Priority> priority, Optional<Boolean> assignedOnly) {
        return store.values().stream()
                .filter(t -> q.map(s -> contains(t, s)).orElse(true))
                .filter(t -> status.map(s -> t.status == s).orElse(true))
                .filter(t -> priority.map(p -> t.priority == p).orElse(true))
                .filter(t -> assignedOnly.map(a -> !t.assignedTo.isEmpty()).orElse(true))
                .sorted(Comparator.comparingInt((Ticket t) -> t.priority.rank).reversed()
                        .thenComparingLong(t -> -t.createdOn))
                .collect(Collectors.toList());
    }

    private boolean contains(Ticket t, String q){
        String L = q.toLowerCase();
        return (t.title!=null && t.title.toLowerCase().contains(L))
                || (t.description!=null && t.description.toLowerCase().contains(L))
                || (t.createdBy!=null && t.createdBy.toLowerCase().contains(L))
                || (t.assignedTo!=null && t.assignedTo.toLowerCase().contains(L))
                || t.status.name().toLowerCase().equals(L)
                || t.priority.name().toLowerCase().equals(L);
    }

    public Ticket create(Ticket in){
        int id = nextId++;
        Ticket t = new Ticket(id, in.title, in.description, in.createdBy);
        if (in.priority != null) t.priority = in.priority;
        store.put(id, t);
        return t;
    }

    public Optional<Ticket> find(int id){ return Optional.ofNullable(store.get(id)); }

    public Optional<Ticket> start(int id){ return updateStatus(id, Status.IN_PROGRESS); }

    public Optional<Ticket> close(int id){
        Optional<Ticket> opt = find(id);
        opt.ifPresent(t -> { t.status = Status.CLOSED; t.closedOn = Instant.now().getEpochSecond(); });
        return opt;
    }

    public Optional<Ticket> assign(int id, String assignee){
        Optional<Ticket> opt = find(id);
        opt.ifPresent(t -> { if (t.status != Status.CLOSED) t.assignedTo = assignee==null ? "" : assignee.trim(); });
        return opt;
    }

    public Map<String,Object> stats(){
        int total = store.size();
        long open   = store.values().stream().filter(t->t.status==Status.OPEN).count();
        long ip     = store.values().stream().filter(t->t.status==Status.IN_PROGRESS).count();
        long closed = store.values().stream().filter(t->t.status==Status.CLOSED).count();

        long low  = store.values().stream().filter(t->t.priority==Priority.LOW).count();
        long med  = store.values().stream().filter(t->t.priority==Priority.MEDIUM).count();
        long high = store.values().stream().filter(t->t.priority==Priority.HIGH).count();

        long closedCount = store.values().stream().filter(t->t.status==Status.CLOSED && t.closedOn>0).count();
        long totalSec = store.values().stream()
                .filter(t->t.status==Status.CLOSED && t.closedOn>0)
                .mapToLong(t->t.closedOn - t.createdOn).filter(s->s>0).sum();
        long avgSec = closedCount==0 ? 0 : totalSec/closedCount;

        Map<String,Object> m = new LinkedHashMap<>();
        m.put("total", total);
        m.put("byStatus", Map.of("OPEN", open, "IN_PROGRESS", ip, "CLOSED", closed));
        m.put("byPriority", Map.of("HIGH", high, "MEDIUM", med, "LOW", low));
        m.put("avgResolutionSeconds", avgSec);
        return m;
    }

    private Optional<Ticket> updateStatus(int id, Status s){
        Optional<Ticket> opt = find(id);
        opt.ifPresent(t -> { if (t.status != Status.CLOSED) t.status = s; });
        return opt;
    }
}

