package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Submission;

public class InCorsoState implements HackathonState {
    @Override
    public String getName() { 
        return "IN CORSO"; 
    }

    @Override
    public void addTeamToHackathon(Hackathon hackathon, Team team) {
        throw new IllegalStateException("Impossibile iscriversi: l'hackathon è già in corso!");
    }

    @Override
    public void removeTeamFromHackathon(Hackathon hackathon, Team team) {
        hackathon.removeTeam(team);
    }

    @Override
    public void sendSubmission(Hackathon hackathon, Team team, Submission submission) {

    }

    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new InValutazioneState());
    }

    @Override
    public void proclaimWinner(Hackathon context, Team team) {
        throw new IllegalStateException("Hackathon ancora in corso");
    }

    @Override
    public void rateSubmission(Hackathon context, Team team, Submission submission, int score, String comment) {
        throw new IllegalStateException("Non è fase di valutazione");
    }

  
}