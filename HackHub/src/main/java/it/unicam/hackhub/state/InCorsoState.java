package main.java.it.unicam.hackhub.state;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Hackathon;

public class InCorsoState {
    @Override
    public String returnState() { return "IN_CORSO"; }

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
        hackathon.setState(new InValutazioneState());
    }
}