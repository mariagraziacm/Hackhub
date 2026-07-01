package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.state.HackathonState;
import it.unicam.hackhub.state.InCorsoState;

public class InIscrizioneState implements HackathonState {
    @Override
    public String returnState() {
        return "IN_ISCRIZIONE";
    }

    @Override
    public void iscriviTeam(Hackathon context, Team team) {
        context.aggiungiTeam(team);
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
    @Override
    public void prossimoStato(Hackathon context){
        context.setHackathonState(new InCorsoState());
    }

    @Override
    public void disiscriviTeam(Hackathon context, Team team) {
        if (context.getTeamIscritti().contains(team)) {
            context.getTeamIscritti().remove(team);
            team.setHackathonId(null);
            System.out.println("SYSTEM: Il team '" + team.getName() + "' si è disiscritto con successo dall'Hackathon.");
        } else {
            System.out.println("SYSTEM [ERRORE]: Il team non risulta iscritto a questo Hackathon.");
        }
}