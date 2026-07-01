package it.unicam.hackhub.state;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;

public interface HackathonState{
    String getName();
    void iscriviTeam(Hackathon context, Team team);
    void disiscriviTeam(Hackathon context, Team team);
    void next(Hackathon context);
}