package it.unicam.hackhub.model;

import it.unicam.hackhub.state.HackathonState;
import it.unicam.hackhub.state.InIscrizioneState;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Hackathon {
    private final String id;
    private String name;
    private HackathonState state;

    private final List<Team> teams = new ArrayList<>();

    private String rules;
    private String specifications;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime registrationDeadline;
    private String place;
    private int prize;

    public Hackathon(String id, String name, String specifications){
        this.id = id;
        this.name = name;
        this.specifications = specifications;
        this.state = new InIscrizioneState();
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public HackathonState getState() { return state; }

    public List<Team> getTeams() {
        return List.copyOf(teams);
    }

    public void setState(HackathonState state) {
        this.state = state;
    }

    public void addTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public void removeTeam(Team team) {
        teams.remove(team);
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
}