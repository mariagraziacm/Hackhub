package it.unicam.hackhub;

import it.unicam.hackhub.model.*;
import it.unicam.hackhub.state.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("TEST HACKHUB - PRIMA ITERAZIONE");

        BuilderHackathon builder = new BuilderHackathon();
        Hackathon mioHackathon = builder
                .setData("HACK-01", "Hackathon Camerino 2026", 5, "Sviluppo software in Java 25")
                .setHackathonState()
                .build();

        System.out.println("Hackathon creato: " + mioHackathon.getName());
        System.out.println("Stato iniziale: " + mioHackathon.getHackathonState().returnState());


        User utenteLeader = new User("Mario", "Rossi", "mario@test.com", "password123", "USR-01");
        TeamMember membroLeader = new TeamMember("TEAM-01", "TEAM-ALPHA", null, utenteLeader);
        Leader leader = new Leader("Caposquadra", membroLeader);


        Team teamAlpha = new Team("TEAM-ALPHA", "Alpha Slayers");
        System.out.println("Team creato: " + teamAlpha.getName() + " da " + leader.getTeamMember().getUser().getName());


        mioHackathon.iscriviTeam(teamAlpha);

        System.out.println("Numero di team iscritti all'hackathon: " + mioHackathon.getTeamIscritti().size());
    }
}