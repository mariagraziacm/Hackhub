package main.java.it.unicam.hackhub.state;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.state.HackathonState;

public class InCorsoState implements HackathonState {
    @Override
    public String getName() { return "IN_CORSO"; }

    @Override
    public void iscriviTeam(Hackathon context, Team team) {
        throw new IllegalStateException("Impossibile iscriversi: l'hackathon è già in corso!");
    }


    @Override
    public void disiscriviTeam(Hackathon hackathon, Team team) {
        hackathon.removeTeam(team);
    }

    @Override
    public void next(Hackathon context) {
        context.setState(new InValutazioneState());
    }
}