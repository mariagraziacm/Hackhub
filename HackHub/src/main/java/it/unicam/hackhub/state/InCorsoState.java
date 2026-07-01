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
    public void disiscriviTeam(Hackathon context, Team team) {
        if (context.getTeamIscritti().contains(team)) {
            context.getTeamIscritti().remove(team);
            team.setHackathonId(null);
            System.out.println("SYSTEM: Il team '" + team.getName() + "' si è disiscritto con successo dall'Hackathon.");
        } else {
            System.out.println("SYSTEM [ERRORE]: Il team non risulta iscritto a questo Hackathon.");
        }
    }

    @Override
    public void prossimoStato(Hackathon context) {
    }
}