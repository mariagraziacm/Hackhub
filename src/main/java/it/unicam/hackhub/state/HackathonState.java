package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Submission;

public interface HackathonState {
    String getName();
    void addTeamToHackathon(Hackathon context, Team team);
    void removeTeamFromHackathon(Hackathon context, Team team);
    void sendSubmission(Hackathon context, Team team, Submission submission);
    void next(Hackathon context);
    void proclamaVincitore(Hackathon context, Team team);
    void valutaSottomissione(Hackathon hackathon, Team team, Submission submission, int score, String comment);
}