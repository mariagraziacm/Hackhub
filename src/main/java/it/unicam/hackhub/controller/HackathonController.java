package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.service.HackathonService;
import it.unicam.hackhub.model.Submission;

import java.util.List;

public class HackathonController {
    private final HackathonService hackathonService;

    public HackathonController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    public void newHackathon(String id, String name, String specifications, String organizerId) {
        try {
            Hackathon nuovoHackathon = hackathonService.createHackathon(id, name, specifications, organizerId);
            System.out.println("SYSTEM: Hackathon '" + name + "' creato con successo e impostato in fase '"
                    + nuovoHackathon.getState().getName()+ "'.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    public void addTeamToHackathon(String hackathonId, String teamId) {
        try {
            hackathonService.addTeamToHackathon(hackathonId, teamId);
            System.out.println("OK iscrizione");
        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }
    public void viewResults(String hackathonId, String organizerId) {
        try {
            List<Submission> results =
                    hackathonService.getResults(hackathonId, organizerId);

            if (results.isEmpty()) {
                System.out.println("Nessun risultato disponibile");
                return;
            }

            results.forEach(r -> System.out.println(
                    "Team: " + r.getTeamId() +
                            " | Score: " + r.getScore() +
                            " | Comment: " + r.getComment()
            ));

        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }
    public void viewIscrizioni(String hackathonId, String organizerId) {
        try {
            List<Team> teams = hackathonService.getIscrizioni(hackathonId, organizerId);

            System.out.println("=== TEAM ISCRITTI ===");

            teams.forEach(t -> System.out.println(
                    "Team: " + t.getName() +
                            " | Membri: " + t.getMembers().size()
            ));

        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }
    public void viewStorico(String staffId) {
        try {
            List<Hackathon> storico = hackathonService.getStoricoStaff(staffId);

            System.out.println("=== STORICO HACKATHON ===");

            storico.forEach(h -> System.out.println(
                    "Hackathon: " + h.getName() +
                            " | Stato: " + h.getState().getName()
            ));

        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }
}