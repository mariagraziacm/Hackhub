package it.unicam.hackhub.model;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    public void addMember(TeamMember member) {
        if (isFull()) {
            throw new IllegalStateException("Team pieno");
        }

        if (hasUser(member.getUser().getId())) {
            throw new IllegalStateException("Utente già presente");
        }

        if (member.isLeader() && members.stream().anyMatch(TeamMember::isLeader)) {
            throw new IllegalStateException("Leader già presente");
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
                m -> m.getUser().getId().equals(userId)
        );
    }

    public boolean isLeader(String userId) {
        return getLeader()
                .getUser()
                .getId()
                .equals(userId);
    }

    public TeamMember getLeader() {
        return members.stream()
                .filter(TeamMember::isLeader)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Leader non trovato"));
    }
    public int getSize() {
        return members.size();
    }
}