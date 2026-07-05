package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.service.TeamService;
import it.unicam.hackhub.service.HackathonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    
    private final TeamService teamService;
    private final HackathonService hackathonService;

    public TeamController(TeamService teamService, HackathonService hackathonService) {
        this.teamService = teamService;
        this.hackathonService = hackathonService;
    }

    
    @PostMapping
    public ResponseEntity<?> creaTeam(@RequestBody TeamCreationRequest request) {
        try {
           
            System.out.println("Ricevuta richiesta per team: " + request.getName());

           
            if(request.getCreatore() == null) {
                return ResponseEntity.badRequest().body("Creatore mancante nel JSON!");
            }
            Team nuovoTeam = teamService.createTeam(request.getId(), request.getName(), request.getCreatore());
            return ResponseEntity.ok(nuovoTeam);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body("ISCRIZIONE [ERRORE]: " + e.getMessage());
        }
    }

    
    @DeleteMapping("/{teamId}/hackathons/{hackathonId}")
    public ResponseEntity<String> abbandonaHackathon(
            @PathVariable String teamId,
            @PathVariable String hackathonId,
            @RequestParam String leaderId) {
        try {
            Team team = teamService.getById(teamId);

            if (team.getLeader() == null || !team.getLeader().getUser().getId().equals(leaderId)) {
                return ResponseEntity.badRequest().body("ISCRIZIONE [ERRORE]: Solo il leader puÃ² disiscrivere il team!");
            }

            hackathonService.removeTeamFromHackathon(hackathonId, team.getId());
            return ResponseEntity.ok("SYSTEM: Team rimosso dall'hackathon");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ISCRIZIONE [ERRORE]: " + e.getMessage());
        }
    }

    
    public static class TeamCreationRequest {
        private String id;
        private String name;
        private User creatore;

   
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public User getCreatore() { return creatore; }
        public void setCreatore(User creatore) { this.creatore = creatore; }
    }
}