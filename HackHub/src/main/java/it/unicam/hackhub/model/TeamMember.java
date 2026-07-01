package it.unicam.hackhub.model;

import it.unicam.hackhub.model.User;

public class TeamMember {
    private final String id;
    private final User user;
    private final Role role;

    public TeamMember(String id, User user, Role role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public Role getRole() { return role; }

    public boolean isLeader() {
        return role == Role.LEADER;
    }
}