package it.unicam.hackhub.external.Calendar;

import java.time.LocalDateTime;

public class TimeSlot {
    private String id;
    private String mentorId;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean available;
    public TimeSlot(String id, String mentorId, LocalDateTime start, LocalDateTime end, boolean available) {
    this.id = id;
    this.mentorId = mentorId;
    this.start = start;
    this.end = end;
    this.available = available;
}

public String getId() { return id; }
public String getMentorId() { return mentorId; }
public LocalDateTime getStart() { return start; }
public LocalDateTime getEnd() { return end; }
public boolean isAvailable() { return available; }
}
