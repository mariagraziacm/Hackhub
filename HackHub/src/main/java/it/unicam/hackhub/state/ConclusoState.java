package main.java.it.unicam.hackhub.state;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Hackathon;

public class ConclusoState {
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
    public void next(Hackathon hackathon) {
        throw new IllegalStateException("Non ci sono stati successivi");
    }
}
