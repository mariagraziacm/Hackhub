package it.unicam.hackhub.model;

public class SupportRequest {
    public enum SupportStatus {
        PENDING,
        ANSWERED
    }

    private final String id;
    private final String hackathonId;
    private final String teamId;
    private final String mentorId;
    private final String message;
    private String callId;


    private SupportStatus status = SupportStatus.PENDING;

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

    public void markAnswered() {
        this.status = SupportStatus.ANSWERED;
    }
    public String getCallId() {
        return callId;
    }
    public void setCallId(String callId) {
        this.callId = callId;
    }
}