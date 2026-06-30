package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;

public class InIscrizioneState implements HackathonState {
    @Override
    public String returnState() {
        return "IN_ISCRIZIONE";
    }

    @Override
    public void iscriviTeam(Hackathon context, Team team) {
        context.getTeamIscritti().add(team);
        System.out.println("Team " + team.getName() + " iscritto con successo!");
    }

    @Override
    public void inviaSottomissione(Hackathon context, Team team, String contenuto) {
        throw new IllegalStateException("Non si possono inviare sottomissioni in fase di iscrizione!");
    }

    @Override
    public void prossimoStato(Hackathon context) {
        System.out.println("Passaggio allo stato successivo non ancora implementato completamente.");
    }
}