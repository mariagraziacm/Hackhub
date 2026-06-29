package it.unicam.hackhub.model;

public class TeamMember {
    private String id;
    private String teamId;
    private String mentorId;

    public TeamMember(String id, String teamId, String mentorId) {
        this.id = id;
        this.teamId = teamId;
        this.mentorId = mentorId;
    }

    public String getId() {
        return id;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getMentorId() {
        return mentorId;
    }
}