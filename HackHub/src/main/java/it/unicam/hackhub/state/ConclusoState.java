package main.java.it.unicam.hackhub.state;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Hackathon;

public class ConclusoState {


    @Override
    public void disiscriviTeam(Hackathon context, Team team) {
        throw new IllegalStateException("Impossibile abbandonare: l'hackathon non è più in fase di iscrizione!");
    }
}
