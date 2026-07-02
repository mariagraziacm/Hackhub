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

        if (hasUser(member.getUserId())) {
            throw new IllegalStateException("Utente già presente");
        }

        if (member.isLeader()) {

            boolean alreadyLeader =
                    members.stream()
                            .anyMatch(TeamMember::isLeader);

            if (alreadyLeader) {
                throw new IllegalStateException("Leader già presente");
            }
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

    public void removeMember(String userId) {

        members.removeIf(
                m -> m.getUserId().equals(userId)
        );
    }

    public boolean isLeader(String userId) {
        return getLeader()
                .getUserId()
                .equals(userId);
    }

    public TeamMember getLeader() {
        return members.stream()
                .filter(TeamMember::isLeader)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Leader non trovato"));
    }
}