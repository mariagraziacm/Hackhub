package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Submission;

public class ConclusoState implements HackathonState {
    @Override
    public String getName() {
        return "CONCLUSO";
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        throw new IllegalStateException("Hackathon concluso");
    }

    @Override
    public void disiscriviTeam(Hackathon hackathon, Team team) {
        throw new IllegalStateException("Hackathon concluso");
    }

    @Override
    public void inviaSottomissione(Hackathon hackathon, Team team, Submission submission) {
        throw new IllegalStateException("Impossibile inviare sottomissioni: l'hackathon è concluso!");
    }

    @Override
    public void next(Hackathon hackathon) {
        throw new IllegalStateException("Non ci sono stati successivi");
    }
}