package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Submission;

public class InCorsoState implements HackathonState {
    @Override
    public String getName() { 
        return "IN CORSO"; 
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        throw new IllegalStateException("Impossibile iscriversi: l'hackathon è già in corso!");
    }

    @Override
    public void disiscriviTeam(Hackathon hackathon, Team team) {
        hackathon.removeTeam(team);
    }

    @Override
    public void inviaSottomissione(Hackathon hackathon, Team team, Submission submission) {
       
    }

    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new InValutazioneState());
    }
}