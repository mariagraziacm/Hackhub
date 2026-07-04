package it.unicam.hackhub.model;

import it.unicam.hackhub.state.HackathonState;
import it.unicam.hackhub.state.InIscrizioneState;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hackathons")
public class Hackathon {

    @Id
    private String id; // Rimosso final per JPA
    
    private String name;

    // Gestione dello State Pattern: salviamo il nome della classe dello stato come stringa nel DB
    @Column(name = "state")
    private String stateClassName;

    @Transient // Dice a JPA di ignorare questo campo, lo gestiamo noi a specchio con stateClassName
    private HackathonState state;

    // Un Hackathon ha molti Team iscritti. 
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "hackathon_teams",
        joinColumns = @JoinColumn(name = "hackathon_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams = new ArrayList<>(); // Rimosso final per JPA

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "winner_id")
    private Team winner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "judge_id")
    private Judge judge;

    // Un Hackathon ha molti Mentor dedicati.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "hackathon_mentors",
        joinColumns = @JoinColumn(name = "hackathon_id"),
        inverseJoinColumns = @JoinColumn(name = "mentor_id")
    )
    private List<Mentor> mentors = new ArrayList<>(); // Rimosso final per JPA

    @Lob // Specifica che il testo può essere molto lungo (Large Object)
    private String specifications;

    // Costruttore vuoto obbligatorio per Spring Boot / JPA
    public Hackathon() {
    }

    public Hackathon(String id, String name, String specifications){
        this.id = id;
        this.name = name;
        this.specifications = specifications;
        this.setState(new InIscrizioneState());
    }

    // --- METODO DI POST-LOAD PER IL DATABASE ---
    // Ricostruisce l'oggetto State corretto partendo dalla stringa salvata nel DB quando JPA carica l'entità
    @PostLoad
    protected void onPostLoad() {
        if (stateClassName != null) {
            try {
                this.state = (HackathonState) Class.forName(stateClassName).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                this.state = new InIscrizioneState(); // Fallback sicuro
            }
        }
    }

    // --- GETTER E SETTER ADATTATI ---
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

    public List<Team> getTeams() { return java.util.Collections.unmodifiableList(teams); }
    
    public void addTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }
    
    public List<Team> getIscritti() {
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

    // --- LA TUA LOGICA DI BUSINESS RIMANE INTATTA ---

    public void iscriviTeam(Team team) {
        getState().iscriviTeam(this, team);
    }

    public void disiscriviTeam(Team team) {
        getState().disiscriviTeam(this, team);
    }

    public void nextState() {
        getState().next(this);
    }
    
    public void inviaSottomissione(Team team, Submission submission) {
        getState().inviaSottomissione(this, team, submission);
    }

    public void proclamaVincitore(Team team) {
        getState().proclamaVincitore(this, team);
    }

    public void valutaSottomissione(Team team, Submission submission, int score, String comment) {
        getState().valutaSottomissione(this, team, submission, score, comment);
    }
}