package it.unicam.hackhub.model;

import it.unicam.hackhub.state.HackathonState;
import it.unicam.hackhub.state.InIscrizioneState;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hackathons")
public class Hackathon{

    @Id
    private String id;
    
    private String name;

    // Gestione dello State Pattern
    @Column(name = "state")
    private String stateClassName;

    @Transient
    private HackathonState state;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "hackathon_teams",
        joinColumns = @JoinColumn(name = "hackathon_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "winner_id")
    private Team winner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "judge_id")
    private Judge judge;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "hackathon_mentors",
        joinColumns = @JoinColumn(name = "hackathon_id"),
        inverseJoinColumns = @JoinColumn(name = "mentor_id")
    )
    private List<Mentor> mentors = new ArrayList<>();

    @Lob
    private String specifications;

    public Hackathon() {
    }

    public Hackathon(String id, String name, String specifications){
        this.id = id;
        this.name = name;
        this.specifications = specifications;
        this.setState(new InIscrizioneState());
    }

    @PostLoad
    protected void onPostLoad() {
        if (stateClassName != null) {
            try {
                this.state = (HackathonState) Class.forName(stateClassName).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                this.state = new InIscrizioneState();
            }
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecifications() { return specifications; }
    
    public HackathonState getState() { 
        if (this.state == null && this.stateClassName != null) {
            onPostLoad();
        }
        return state; 
    }
    
    public void setState(HackathonState state) { 
        this.state = state; 
        if (state != null) {
            this.stateClassName = state.getClass().getName();
        }
    }

    public Organizer getOrganizer() { return organizer; }
    public void setOrganizer(Organizer organizer) { this.organizer = organizer; }

    public Judge getJudge() { return judge; }
    public void setJudge(Judge judge) { this.judge = judge; }

    public List<Mentor> getMentors() { return java.util.Collections.unmodifiableList(mentors); }
    public void addMentor(Mentor mentor) {
        if (!mentors.contains(mentor)) {
            mentors.add(mentor);
        }
    }

    public void addTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }
    
    public List<Team> getPartecipantTeams() {
        return java.util.Collections.unmodifiableList(teams);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public Team getWinner(){ return winner; }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }


    public void addTeamToHackathon(Team team) {
        getState().addTeamToHackathon(this, team);
    }

    public void removeTeamFromHackathon(Team team) {
        getState().removeTeamFromHackathon(this, team);
    }

    public void nextState() {
        getState().next(this);
    }
    
    public void sendSubmission(Team team, Submission submission) {
        getState().sendSubmission(this, team, submission);
    }

    public void proclaimWinner(Team team) {
        getState().proclaimWinner(this, team);
    }

    public void rateSubmission(Team team, Submission submission, int score, String comment) {
        getState().rateSubmission(this, team, submission, score, comment);
    }
 
}