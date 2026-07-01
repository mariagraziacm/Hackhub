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

    public void iscriviTeamAdHackathon(String teamId, String hackathonId) {
        try {
            Team team = teamService.getById(teamId);
            hackathonService.iscriviTeam(team, hackathonId);
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("ISCRIZIONE [ERRORE]: " + e.getMessage());
        }
    }

    public void abbandonaHackathon(String idLeaderUser, String teamId, String hackathonId) {
        try {
            Team team = teamService.getById(teamId);

            if (team.getLeader() == null || !team.getLeader().getTeamMember().getUser().getId().equals(idLeaderUser)) {
                System.out.println("ISCRIZIONE [ERRORE]: Solo il leader del team può disiscrivere il team dall'hackathon!");
                return;
            }

            hackathonService.disiscriviTeam(team, hackathonId);
            System.out.println("SYSTEM: Il team '" + team.getName() + "' non è più iscritto all'hackathon.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("ISCRIZIONE [ERRORE]: " + e.getMessage());
        }
    }
}