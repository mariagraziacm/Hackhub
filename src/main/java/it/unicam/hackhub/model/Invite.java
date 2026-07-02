package it.unicam.hackhub.model;

public class Invite {
    private final String id;
    private final User user;

    // Caso 1: invito team
    private final Team team;

    // Caso 2: invito hackathon (mentor/judge)
    private final String hackathonId;
    private final String inviteType; // "TEAM", "MENTOR", "JUDGE"

    private InviteState state;

    // Costruttore esistente: invito team
    public Invite(String id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.hackathonId = null;
        this.inviteType = "TEAM";
        this.state = InviteState.PENDING;
    }

    // Nuovo costruttore: invito mentor/judge
    public Invite(String id, User user, String hackathonId, String inviteType) {
        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalStateException("hackathonId obbligatorio");
        }
        if (inviteType == null || inviteType.isBlank()) {
            throw new IllegalStateException("inviteType obbligatorio");
        }
        if (!inviteType.equals("MENTOR") && !inviteType.equals("JUDGE")) {
            throw new IllegalStateException("inviteType non valido");
        }

        this.id = id;
        this.user = user;
        this.team = null;
        this.hackathonId = hackathonId;
        this.inviteType = inviteType;
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
    public String getInviteType() { return inviteType; }
    public InviteState getState() { return state; }

    public boolean isTeamInvite() {
        return "TEAM".equals(inviteType);
    }

    public boolean isHackathonInvite() {
        return "MENTOR".equals(inviteType) || "JUDGE".equals(inviteType);
    }
}