package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Giudice;

public class InIscrizioneState implements  HackathonState{

    @Override
    public void iscriviTeam(Hackathon HackathonCorrente, Team team){
        HackathonCorrente.getTeamIscritti().add(team);
        System.out.println("Team " + team.getNome() + "iscritto con successo!")
    }

    @Override
    public void inviaSottomissione(Hackathon HackathonCorrente, Team team, String contenuto) {
        throw new IllegalStateException("Errore, non puoi inviare sottomissione durante la fase di iscrizione");
    }

    @Override
    public void valutaSottomissione(Hackathon HackathonCorrente, Giudice giudice, Team team, float voto, String contenuto) {
        throw new IllegalStateException("Errore, non puoi valutare una sottomissione edurante la fase di iscrizone");
    }

    @Override
    public void prossimoStato(Hackathon HackathonCorrente) {
        HackathonCorrente.setStato(new inCorsoState());
        System.out.println("L'Hackathon è ora in corso!");
    }
}
