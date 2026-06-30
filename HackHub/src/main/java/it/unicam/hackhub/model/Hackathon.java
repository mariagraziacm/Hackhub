package it.unicam.hackhub.model;

import it.unicam.hackhub.state.HackathonState;
import it.unicam.hackhub.state.InIscrizioneState;
import it.unicam.hackhub.model.Submission;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Hackathon {
    private String name;
    private HackathonState statoHackathon;
    private List<Team> teamIscritti = new ArrayList<>();
    private List<Submission> sottomissioni = new ArrayList<>();
    private String id;
    private List<User> utenti = new ArrayList<>();
    private String rules;
    private int partecipants;
    private String specifications;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime submissionDeadline;
    private LocalDateTime registrationDeadline;
    private String place;
    private int prize;

    public Hackathon() {
        this.statoHackathon = new InIscrizioneState();
    }

    public Hackathon(String name){
        this.name = name;
        this.statoHackathon = new InIscrizioneState();
    }

    public void setHackathonState(HackathonState nuovoStato){
        this.statoHackathon = nuovoStato;
    }

    public HackathonState getHackathonState() {
        return statoHackathon;
    }

    public void iscriviTeam(Team team){
        statoHackathon.iscriviTeam(this, team);
    }

    public void inviaSottomissione(Team team, String contenuto){
        statoHackathon.inviaSottomissione(this, team, contenuto);
    }

    public void avanzaStato(){
        statoHackathon.prossimoStato(this);
    }

    public List<Team> getTeamIscritti(){ return teamIscritti; }
    public String getName(){ return name; }
    public void setId(String id){ this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPartecipants(int partecipants) { this.partecipants = partecipants; }
    public void setSpecifications(String specifications) { this.specifications = specifications; }

    public String getRules() { return rules; }
    public String getSpecifications() { return specifications; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public LocalDateTime getSubmissionDeadline() { return submissionDeadline; }
    public LocalDateTime getRegistrationDeadline() { return registrationDeadline; }
    public String getPlace() { return place; }
    public int getPrize() { return prize; }
    public List<Submission> getSottomissioni() { return sottomissioni; }
    public List<User> getUtenti() { return utenti; }
}