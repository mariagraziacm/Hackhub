package main.java.it.unicam.hackhub.state;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.state.HackathonState;

public class InValutazioneState implements HackathonState{
    @Override
    public String getName() {
        return "IN_VALUTAZIONE";
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
    public void next(Hackathon hackathon) {
        hackathon.setState(new ConclusoState());
    }
}
