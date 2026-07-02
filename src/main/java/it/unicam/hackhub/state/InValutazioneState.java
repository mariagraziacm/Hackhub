package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Submission;

public class InValutazioneState implements HackathonState {
    @Override
    public String getName() {
        return "IN VALUTAZIONE";
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        throw new IllegalStateException("Valutazione in corso");
    }

    @Override
    public void disiscriviTeam(Hackathon hackathon, Team team) {
        throw new IllegalStateException("Non puoi disiscriverti in valutazione");
    }

    @Override
    public void inviaSottomissione(Hackathon hackathon, Team team, Submission submission) {
        throw new IllegalStateException("Impossibile inviare o modificare la sottomissione: l'hackathon è in fase di valutazione!");
    }

    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new ConclusoState());
    }
}