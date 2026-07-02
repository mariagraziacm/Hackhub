package it.unicam.hackhub.model;

public class Invite {
    public enum InviteType {
    TEAM, MENTOR, JUDGE
}
public enum InviteState {
    PENDING,
    ACCEPTED,
    DECLINED
}
    private final String id;
    private final User user;

    // invito team
    private final Team team;

    // invito mentor/judge
    private final String hackathonId;
    private final InviteType inviteType;

    private InviteState state;

    // Costruttore: invito team
    public Invite(String id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.hackathonId = null;
        this.inviteType = InviteType.TEAM;
        this.state = InviteState.PENDING;
    }

    //  costruttore: invito mentor/judge
    public Invite(String id, User user, String hackathonId, String typeStr) {
        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalStateException("hackathonId non valido");
        }
        
        // Converti ("MENTOR" o "JUDGE") nell'enum corrispondente
        InviteType t = InviteType.valueOf(typeStr.toUpperCase());
        
        if (t == null) {
            throw new IllegalStateException("inviteType non valido");
        }

        this.id = id;
        this.user = user;
        this.team = null;
        this.hackathonId = hackathonId;
        this.inviteType = t; // Assegna l'enum convertito
        this.state = InviteState.PENDING;
    }

    public void accept() {
        if (state != InviteState.PENDING) {
            throw new IllegalStateException("Invito non più pending");
        }
        state = InviteState.ACCEPTED;
    }

    public void decline() {
        if (state != InviteState.PENDING) {
            throw new IllegalStateException("Invito non più pending");
        }
        state = InviteState.DECLINED;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public Team getTeam() { return team; }
    public String getHackathonId() { return hackathonId; }
    public InviteType getInviteType() { return inviteType; }
    public InviteState getState() { return state; }

    public boolean isTeamInvite() {
        return InviteType.TEAM.equals(inviteType);
    }

    public boolean isHackathonInvite() {
        return InviteType.MENTOR.equals(inviteType) || InviteType.JUDGE.equals(inviteType);
    }
}