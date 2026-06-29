package it.unicam.hackhub.model;

public class Team {
    private String name;
    private String id;
    private List<User> members;
    private List<Submission> submissions;
    private String hackathonId;

    public Team(String name, String id, List<User> members, List<Submission> submissions, String hackathonId) {
        this.name = name;
        this.id = id;
        this.members = members;
        this.submissions = submissions;
        this.hackathonId = hackathonId;
    }

    public String getName() {
        return name;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<User> getMembers() {
        return members;
    }
    public List<Submission> getSubmissions() {
        return submissions;
    }
    public String getHackathonId() {
        return hackathonId;
    }
}