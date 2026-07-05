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
    public void addTeamToHackathon(Hackathon hackathon, Team team) {
        throw new IllegalStateException("Valutazione in corso");
    }

    @Override
    public void removeTeamFromHackathon(Hackathon hackathon, Team team) {
        throw new IllegalStateException("Non puoi disiscriverti in valutazione");
    }

    @Override
    public void sendSubmission(Hackathon hackathon, Team team, Submission submission) {
        throw new IllegalStateException("Impossibile inviare o modificare la sottomissione: l'hackathon è in fase di valutazione!");
    }

    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new ConclusoState());
    }



    @Override
    public void proclaimWinner      (Hackathon hackathon, Team team) {
        if (!hackathon.getTeams().contains(team)) {
            throw new IllegalStateException("Team non partecipante");
        }

        hackathon.setWinner(team);
        hackathon.setState(new ConclusoState());

        System.out.println("Vincitore proclamato: " + team.getName());
    }

    @Override
    public void rateSubmission(Hackathon context, Team team, Submission submission, int score, String comment) {

        if (submission == null) {
            throw new IllegalStateException("Sottomissione non trovata");
        }

        if (!submission.getTeamId().equals(team.getId())) {
            throw new IllegalStateException("Submission non del team selezionato");
        }

        submission.rate(score, comment);


    }
  
  
}