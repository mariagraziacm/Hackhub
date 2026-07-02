package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Submission;

public class InCorsoState implements HackathonState {
    @Override
    public String getName() { 
        return "IN_CORSO"; 
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        throw new IllegalStateException("Impossibile iscriversi: l'hackathon è già in corso!");
    }

    @Override
    public void disiscriviTeam(Hackathon hackathon, Team team) {
        hackathon.removeTeam(team);
    }

    @Override
    public void inviaSottomissione(Hackathon hackathon, Team team, Submission submission) {
        // Se l'hackathon memorizza le sottomissioni o delega al repository, 
        // l'azione è permessa e valida esclusivamente in questo stato.
        // Nota: Assicurati di aggiungere il meccanismo di salvataggio/mappa sottomissioni se necessario.
    }

    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new InValutazioneState());
    }
}