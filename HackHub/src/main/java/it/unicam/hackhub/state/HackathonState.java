package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackthon;
import it.unicam.hackhub.model.Tema;
import it.unicam.hackhub.model.Giudice;

public interface HackathonState {

    public void prossimoStato(Hackathon HackathonCorrente ){};
    public void iscriviTeam(Hackathon HackathonCorrente, Team team){};
    public void inviaSottomissione(Hackathon HackathonCorrente, Team team, String contenuto){};
    public void valutaSottomissione(Hackathon HackathonCorrente, Giudice giudice, Team team, float voto,  String contenuto){};
}
