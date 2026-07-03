package it.unicam.hackhub.model;

public class Call {
    public enum CallStatus {
        RESERVED,
        WAITING_TEAM_RESPONSE,
        ACCEPTED,
        REJECTED
    }

    private final String id;
    private final String mentorId;
    private final String teamId;
    private final String hackathonId;

    private String slotId;
    private CallStatus status;

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
