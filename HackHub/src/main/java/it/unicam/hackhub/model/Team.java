package it.unicam.hackhub.model;

import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.Submission;
import it.unicam.hackhub.model.Leader;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private String id;
    private List<TeamMember> members = new ArrayList<>();
    private List<Submission> submissions = new ArrayList<>();
    private String hackathonId;
    private Leader leader;
    private int maxMembers;

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
        this.maxMembers = 5;
    }
    public Leader getLeader() { return leader; }
    public void setLeader(Leader leader) { this.leader = leader; }

    public String getName() { return name; }
    public void setId(String id) { this.id = id; }
    public String getId(){
        return id;
    }
    public List<TeamMember> getMembers() { return members; }
    public List<Submission> getSubmissions() { return submissions; }
    public String getHackathonId() { return hackathonId; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
    public boolean isAlCompleto() {
        return members.size() >= maxMembers;
    }
}