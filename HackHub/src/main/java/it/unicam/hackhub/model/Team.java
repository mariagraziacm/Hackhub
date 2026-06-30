package it.unicam.hackhub.model;

import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.Submission;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private String id;
    private List<TeamMember> members = new ArrayList<>();
    private List<Submission> submissions = new ArrayList<>();
    private String hackathonId;

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() { return name; }
    public void setId(String id) { this.id = id; }

    public List<TeamMember> getMembers() { return members; }
    public List<Submission> getSubmissions() { return submissions; }
    public String getHackathonId() { return hackathonId; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
}