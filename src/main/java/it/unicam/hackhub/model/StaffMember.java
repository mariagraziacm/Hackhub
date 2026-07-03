package it.unicam.hackhub.model;

public abstract class StaffMember {
    private final String id;
    private final User user;
    private final String hackathonId;

    public StaffMember(String id, User user, String hackathonId) {
        this.id = id;
        this.user = user;
        this.hackathonId = hackathonId;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public String getHackathonId() { return hackathonId; }
}