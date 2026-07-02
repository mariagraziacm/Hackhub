package it.unicam.hackhub.model;



public class ParticipationRequest {
// ENUM
    public enum ParticipationRequestState {
    PENDING,
    ACCEPTED,
    DECLINED
}
    private final String id;
    private final User user;
    private final Team team;
    private ParticipationRequestState state;

    public ParticipationRequest(String id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.state = ParticipationRequestState.PENDING;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public Team getTeam() { return team; }
    public ParticipationRequestState getState() { return state; }

    public void accept() {
        if (state != ParticipationRequestState.PENDING)
            throw new IllegalStateException("Already processed");
        state = ParticipationRequestState.ACCEPTED;
    }

    public void decline() {
        if (state != ParticipationRequestState.PENDING)
            throw new IllegalStateException("Already processed");
        state = ParticipationRequestState.DECLINED;
    }
}