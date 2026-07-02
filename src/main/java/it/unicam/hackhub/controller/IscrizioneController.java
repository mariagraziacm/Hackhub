package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.service.TeamService;
import it.unicam.hackhub.service.HackathonService;

public class IscrizioneController {
    private final TeamService teamService;
    private final HackathonService hackathonService;

    public IscrizioneController(TeamService teamService, HackathonService hackathonService) {
        this.teamService = teamService;
        this.hackathonService = hackathonService;
    }

    public Team creaTeam(String id, String name, User creatore) {
        try {
            Team nuovoTeam = teamService.createTeam(id, name, creatore);
            System.out.println("ISCRIZIONE: Team '" + name + "' registrato nel sistema. Leader: " + creatore.getName());
            return nuovoTeam;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("ISCRIZIONE [ERRORE]: " + e.getMessage());
            return null;
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

    public void abbandonaHackathon(String idLeaderUser, String teamId, String hackathonId) {
        try {
            Team team = teamService.getById(teamId);

            if (team.getLeader() == null ||
                    !team.getLeader().getUser().getId().equals(idLeaderUser)) {

                System.out.println("ISCRIZIONE [ERRORE]: Solo il leader può disiscrivere il team!");
                return;
            }

            hackathonService.removeTeamFromHackathon(hackathonId, team);

            System.out.println("SYSTEM: Team rimosso dall'hackathon");

        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("ISCRIZIONE [ERRORE]: " + e.getMessage());
        }
    }
}