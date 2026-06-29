package it.unicam.hackhub.model;

public class Leader {
    private String name;
    private TeamMember teamMember;

    public Leader(String name, TeamMember teamMember) {
        this.name = name;
        this.teamMember = teamMember;
    }

    public TeamMember getTeamMember() { return teamMember; }
}
