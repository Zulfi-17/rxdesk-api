package com.example.rxdesk.model;
public enum Priority {
    LOW(1), MEDIUM(2), HIGH(3);
    public final int rank;
    Priority(int rank){ this.rank = rank; }
}
