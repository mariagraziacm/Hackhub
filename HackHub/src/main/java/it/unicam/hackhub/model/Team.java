package it.unicam.hackhub.model;

import it.unicam.hackhub.model.TeamMember;


import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private String id;
    private List<TeamMember> members = new ArrayList<>();
    private final int maxMembers=5;

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public List<TeamMember> getMembers() {
        return List.copyOf(members);
    }

    public void addMember(TeamMember member) {
        if (isFull()) {
            throw new IllegalStateException("Team pieno");
        }
        members.add(member);
    }

    public boolean isFull() {
        return members.size() >= maxMembers;
    }

    public boolean hasUser(String userId) {
        return members.stream()
                .anyMatch(m -> m.getUser().getId().equals(userId));
    }

    public boolean hasMinimumMembers() {
        return members.size() >= 2;
    }

    public TeamMember getLeader() {
        return members.stream()
                .filter(TeamMember::isLeader)
                .findFirst()
                .orElse(null);
    }
}