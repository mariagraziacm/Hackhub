package it.unicam.hackhub.model;

import it.unicam.hackhub.model.TeamMember;


import java.util.ArrayList;
import java.util.List;

public class Team {
    private final String id;
    private String name;
    private final List<TeamMember> members = new ArrayList<>();
    private final int maxMembers = 5;

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

    public boolean hasMinMembers() {
        return members.size() >= 2;
    }

    public boolean hasUser(String userId) {
        return members.stream()
                .anyMatch(m -> m.getUser().getId().equals(userId));
    }
}