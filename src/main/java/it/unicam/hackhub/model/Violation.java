package it.unicam.hackhub.model;

import java.util.Objects;

public class Violation {
    
    // Spostato PUBLIC ENUM all'interno della classe come tipo nidificato
    public enum ViolationStatus {
        PENDING, DISQUALIFY_TEAM, DISQUALIFY_MEMBER, NO_ACTION
    }

    private final String id;
    private final String hackathonId;
    private final String teamId;
    private final String reportedMemberId; // opzionale: può essere null
    private final String mentorId;
    private final String reason;

    // Inizializzazione sicura con lo stato PENDING
    private ViolationStatus status = ViolationStatus.PENDING;

    public Violation(String id,
                     String hackathonId,
                     String teamId,
                     String reportedMemberId,
                     String mentorId,
                     String reason) {
        this.id = requireNotBlank(id, "id obbligatorio");
        this.hackathonId = requireNotBlank(hackathonId, "hackathonId obbligatorio");
        this.teamId = requireNotBlank(teamId, "teamId obbligatorio");
        this.reportedMemberId = reportedMemberId;
        this.mentorId = requireNotBlank(mentorId, "mentorId obbligatorio");
        this.reason = requireNotBlank(reason, "reason obbligatorio");
    }

    public String getId() { return id; }
    public String getHackathonId() { return hackathonId; }
    public String getTeamId() { return teamId; }
    public String getReportedMemberId() { return reportedMemberId; }
    public String getMentorId() { return mentorId; }
    public String getReason() { return reason; }
    public ViolationStatus getStatus() { return status; }

    public boolean isPending() {
        return ViolationStatus.PENDING.equals(status);
    }

    public void setDisqualifyTeam() {
        ensurePending();
        this.status = ViolationStatus.DISQUALIFY_TEAM;
    }

    public void setDisqualifyMember() {
        ensurePending();
        this.status = ViolationStatus.DISQUALIFY_MEMBER;
    }

    public void setNoAction() {
        ensurePending();
        this.status = ViolationStatus.NO_ACTION;
    }

    private void ensurePending() {
        if (!isPending()) {
            throw new IllegalStateException("Violazione già gestita");
        }
    }

    private String requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            // Nota: solitamente per argomenti errati nel costruttore si lancia IllegalArgumentException
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Violation)) return false;
        Violation that = (Violation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}