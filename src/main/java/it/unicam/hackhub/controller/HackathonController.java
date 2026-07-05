package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Submission;
import it.unicam.hackhub.service.HackathonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hackathons")
public class HackathonController {
    
    private final HackathonService hackathonService;

    public HackathonController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    // POST /api/hackathons -> Crea un nuovo hackathon
    @PostMapping
    public ResponseEntity<String> newHackathon(@RequestBody HackathonRegisterPayload payload) {
        try {
            Hackathon nuovoHackathon = hackathonService.createHackathon(
                    payload.getId(), 
                    payload.getName(), 
                    payload.getSpecifications(), 
                    payload.getOrganizerId()
            );
            return ResponseEntity.ok("SYSTEM: Hackathon '" + payload.getName() + "' creato con successo e impostato in fase '"
                    + nuovoHackathon.getState().getName() + "'.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    // POST /api/hackathons/teams -> Iscrive un team a un hackathon
    @PostMapping("/teams")
    public ResponseEntity<String> addTeamToHackathon(@RequestBody TeamEnrollmentPayload payload) {
        try {
            hackathonService.addTeamToHackathon(payload.getHackathonId(), payload.getTeamId());
            return ResponseEntity.ok("OK iscrizione");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    // GET /api/hackathons/{hackathonId}/results?organizerId=XYZ -> Visualizza i risultati delle sottomissioni
    @GetMapping("/{hackathonId}/results")
    public ResponseEntity<?> viewResults(
            @PathVariable String hackathonId, 
            @RequestParam String organizerId) {
        try {
            List<Submission> results = hackathonService.getResults(hackathonId, organizerId);
            if (results.isEmpty()) {
                return ResponseEntity.ok("Nessun risultato disponibile");
            }
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    // GET /api/hackathons/{hackathonId}/registrations?organizerId=XYZ -> Visualizza i team iscritti
    @GetMapping("/{hackathonId}/registrations")
    public ResponseEntity<?> viewIscrizioni(
            @PathVariable String hackathonId, 
            @RequestParam String organizerId) {
        try {
            List<Team> teams = hackathonService.getIscrizioni(hackathonId, organizerId);
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    // GET /api/hackathons/history/staff/{staffId} -> Visualizza lo storico degli hackathon dello staff
    @GetMapping("/history/staff/{staffId}")
    public ResponseEntity<?> viewStorico(@PathVariable String staffId) {
        try {
            List<Hackathon> storico = hackathonService.getStoricoStaff(staffId);
            return ResponseEntity.ok(storico);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    // DELETE /api/hackathons/{hackathonId}/teams/{teamId} -> Rimuove un team da un hackathon
    @DeleteMapping("/{hackathonId}/teams/{teamId}")
    public ResponseEntity<String> removeTeamFromHackathon(
            @PathVariable String hackathonId,
            @PathVariable String teamId) {
        try {
            hackathonService.removeTeamFromHackathon(hackathonId, teamId);
            return ResponseEntity.ok("Team rimosso con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    // DTO per la registrazione dell'hackathon
    public static class HackathonRegisterPayload {
        private String id;
        private String name;
        private String specifications;
        private String organizerId;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSpecifications() { return specifications; }
        public void setSpecifications(String specifications) { this.specifications = specifications; }
        public String getOrganizerId() { return organizerId; }
        public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }
    }

    // DTO per l'iscrizione del team
    public static class TeamEnrollmentPayload {
        private String hackathonId;
        private String teamId;

        public String getHackathonId() { return hackathonId; }
        public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
        public String getTeamId() { return teamId; }
        public void setTeamId(String teamId) { this.teamId = teamId; }
    }
}