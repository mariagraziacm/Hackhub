package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "support_requests")
public class SupportRequest {

   
    public enum SupportStatus {
        PENDING,
        ANSWERED
    }

    @Id
    private String id;
    
    private String hackathonId; 
    private String teamId; 
    private String mentorId; 

    @Lob 
    private String message; 
    
    private String callId;

    @Enumerated(EnumType.STRING) 
    private SupportStatus status = SupportStatus.PENDING;

    
    public SupportRequest() {
    }

    public SupportRequest(String id,
                          String hackathonId,
                          String teamId,
                          String mentorId,
                          String message) {
        this.id = id;
        this.hackathonId = hackathonId;
        this.teamId = teamId;
        this.mentorId = mentorId;
        this.message = message;
    }

   
    public String getId() { return id; }
    public String getHackathonId() { return hackathonId; }
    public String getTeamId() { return teamId; }
    public String getMentorId() { return mentorId; }
    public String getMessage() { return message; }
    public SupportStatus getStatus() { return status; }
    public String getCallId() { return callId; }

    
    public void setId(String id) { this.id = id; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    public void setMentorId(String mentorId) { this.mentorId = mentorId; }
    public void setMessage(String message) { this.message = message; }
    public void setStatus(SupportStatus status) { this.status = status; }
    public void setCallId(String callId) { this.callId = callId; }

    

    public void markAnswered() {
        this.status = SupportStatus.ANSWERED;
    }
}