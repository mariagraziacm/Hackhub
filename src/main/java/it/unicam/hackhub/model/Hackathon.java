package it.unicam.hackhub.model;

import it.unicam.hackhub.state.HackathonState;
import it.unicam.hackhub.state.InIscrizioneState;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Hackathon {
    private final String id;
    private String name;
    private HackathonState state;
    private final List<Team> teams = new ArrayList<>();

    private Organizer organizer;
    private Judge judge;
    private final List<Mentor> mentors = new ArrayList<>();
    private String specifications;

    public Hackathon(String id, String name, String specifications){
        this.id = id;
        this.name = name;
        this.specifications = specifications;
        this.state = new InIscrizioneState();
    }

    public Organizer getOrganizer() { return organizer; }
    public void setOrganizer(Organizer organizer) { this.organizer = organizer; }

    public Judge getJudge() { return judge; }
    public void setJudge(Judge judge) { this.judge = judge; }

    public List<Mentor> getMentors() { return List.copyOf(mentors); }
    public void addMentor(Mentor mentor) {
        if (!mentors.contains(mentor)) {
            mentors.add(mentor);
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public HackathonState getState() { return state; }
    public List<Team> getTeams() { return List.copyOf(teams); }
    public void setState(HackathonState state) { this.state = state; }
    
    public void addTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public void iscriviTeam(Team team) {
        state.iscriviTeam(this, team);
    }

    public void disiscriviTeam(Team team) {
        state.disiscriviTeam(this, team);
    }

    public void nextState() {
        state.next(this);
    }
    
    public void inviaSottomissione(Team team, Submission submission) {
        state.inviaSottomissione(this, team, submission);
    }
}