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


    @PostMapping
    public ResponseEntity<String> createHackathon(@RequestBody HackathonController.HackathonCreationPayload payload) {
        try {
            Hackathon h = hackathonService.createHackathon(
                    payload.getId(),
                    payload.getName(),
                    payload.getSpecifications(),
                    payload.getOrganizerId()
            );
            return ResponseEntity.ok("SYSTEM [SUCCESS]: Hackathon '" + h.getName() + "' creato dall'organizzatore.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }



    @PostMapping("/hackathons/mentors")
    public ResponseEntity<String> addMentor(@RequestBody HackathonController.AssociationPayload payload) {
        try {
            hackathonService.addMentorToHackathon(
                    payload.getHackathonId(),
                    payload.getTargetId(),
                    payload.getOrganizerId()
            );
            return ResponseEntity.ok("SYSTEM [SUCCESS]: Mentore aggiunto con successo all'hackathon.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    
    @PostMapping("/teams")
    public ResponseEntity<String> addTeamToHackathon(@RequestBody TeamEnrollmentPayload payload) {
        try {
            hackathonService.addTeamToHackathon(payload.getHackathonId(), payload.getTeamId());
            return ResponseEntity.ok("OK iscrizione");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    
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

    
    @GetMapping("/{hackathonId}/registrations")
    public ResponseEntity<?> viewIscrizioni(
            @PathVariable String hackathonId, 
            @RequestParam String organizerId) {
        try {
            List<Team> teams = hackathonService.getPartecipants(hackathonId, organizerId);
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

   
    @GetMapping("/history/staff/{staffId}")
    public ResponseEntity<?> viewStorico(@PathVariable String staffId) {
        try {
            List<Hackathon> storico = hackathonService.getHistoryStaff(staffId);
            return ResponseEntity.ok(storico);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    @PutMapping("/hackathons/winner")
    public ResponseEntity<String> proclaimWinner(@RequestBody HackathonController.AssociationPayload payload) {
        try {
            hackathonService.proclaimWinner(
                    payload.getHackathonId(),
                    payload.getTargetId(),
                    payload.getOrganizerId()
            );
            return ResponseEntity.ok("Vincitore proclamato con successo!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    
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

    public static class HackathonCreationPayload {
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


    public static class AssociationPayload {
        private String hackathonId;
        private String targetId;
        private String organizerId;

        public String getHackathonId() { return hackathonId; }
        public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public String getOrganizerId() { return organizerId; }
        public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }
    }

    public static class TeamEnrollmentPayload {
        private String hackathonId;
        private String teamId;

        public String getHackathonId() { return hackathonId; }
        public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
        public String getTeamId() { return teamId; }
        public void setTeamId(String teamId) { this.teamId = teamId; }
    }
}