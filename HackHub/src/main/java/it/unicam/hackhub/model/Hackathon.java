package it.unicam.hackhub.model;

import it.unicam.hackhub.state.HackathonState;
import it.unicam.hackhub.state.inIscrizioneState;
import java.util.ArrayList;
import java.util.List;


public class Hackathon {
    private String nome;
    private HackathonState statoHackathon;
    private list<Team> teamIscritti=new ArrayList<>();
    private List<Sottomissione> sottomissioni=new ArrayList<>();

    public Hackathon(String nome){
        this.nome=nome;
        this.statoHackathon=new inIscrizioneState();
    }
    public void setStato(HackathonState nuovoStato){
        this.statoHackathon=nuovoStato;
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
    public List<Team> getTeamIscritti(){
        return teamIscritti;
    }
    public String getNome(){
        return nome;
    }
}
