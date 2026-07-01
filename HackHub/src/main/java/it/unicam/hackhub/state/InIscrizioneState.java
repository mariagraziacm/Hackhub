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
    public void next(Hackathon hackathon) {
        hackathon.setState(new InCorsoState());
    }
}