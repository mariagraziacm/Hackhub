package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Submission;

public class InIscrizioneState implements HackathonState {
    @Override
    public String getName() {
        return "IN ISCRIZIONE";
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        if (team.isFull()) {
            throw new IllegalStateException("Team pieno");
        }
        hackathon.addTeam(team);
    }

    @Override
    public void disiscriviTeam(Hackathon hackathon, Team team) {
        hackathon.removeTeam(team);
    }

    @Override
    public void inviaSottomissione(Hackathon hackathon, Team team, Submission submission) {
        throw new IllegalStateException("Impossibile inviare la sottomissione: l'hackathon non è ancora in corso!");
    }

    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new InCorsoState());
    }

    @Override
    public void proclamaVincitore(Hackathon context, Team team) {
        throw new IllegalStateException("Non puoi proclamare vincitore in iscrizione");
    }

    @Override
    public void valutaSottomissione(Hackathon context, Team team, Submission submission, int score, String comment) {
        throw new IllegalStateException("Non è fase di valutazione");
    }
}