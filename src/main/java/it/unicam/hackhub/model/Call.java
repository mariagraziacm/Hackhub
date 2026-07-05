package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "calls")
public class Call {

    public enum CallStatus {
        RESERVED,
        WAITING_TEAM_RESPONSE,
        ACCEPTED,
        REJECTED
    }

    @Id
    private String id; 
    
    private String mentorId; 
    private String teamId; 
    private String hackathonId; 

    private String slotId;

    @Enumerated(EnumType.STRING) 
    private CallStatus status;

   
    public Call() {
    }

    public Call(String id, String mentorId, String teamId, String hackathonId, String slotId) {
        this.id = id;
        this.mentorId = mentorId;
        this.teamId = teamId;
        this.hackathonId = hackathonId;
        this.slotId = slotId;
        this.status = CallStatus.RESERVED;
    }


    public String getId() {
        return id;
    }

    public String getMentorId() {
        return mentorId;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getHackathonId() {
        return hackathonId;
    }

    public String getSlotId() {
        return slotId;
    }

    public CallStatus getStatus() {
        return status;
    }


    public void setId(String id) { this.id = id; }
    public void setMentorId(String mentorId) { this.mentorId = mentorId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }
    public void setStatus(CallStatus status) { this.status = status; }



    public void setWaitingResponse() {
        this.status = CallStatus.WAITING_TEAM_RESPONSE;
    }

    public void accept() {
        if (status != CallStatus.WAITING_TEAM_RESPONSE) {
            throw new IllegalStateException("Call non in stato accettabile");
        }
        this.status = CallStatus.ACCEPTED;
    }

    public void reject() {
        if (status != CallStatus.WAITING_TEAM_RESPONSE) {
            throw new IllegalStateException("Call non in stato rifiutabile");
        }
        this.status = CallStatus.REJECTED;
    }
}