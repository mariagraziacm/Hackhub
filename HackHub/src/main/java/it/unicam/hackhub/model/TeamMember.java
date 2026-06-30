package it.unicam.hackhub.model;

import it.unicam.hackhub.model.User;

public class TeamMember {
    private String id;
    private String teamId;
    private String mentorId;
    private User user;

    public TeamMember(String id, String teamId, String mentorId, User user) {
        this.id = id;
        this.teamId = teamId;
        this.mentorId = mentorId;
        this.user = user;
    }

    public String getId() { return id; }
    public String getTeamId() { return teamId; }
    public String getMentorId() { return mentorId; }
    public User getUser() { return user; }
}