package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.service.HackathonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizer")
public class OrganizerController {
    
    private final HackathonService hackathonService;

    public OrganizerController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    // POST /api/organizer/hackathons -> Crea un nuovo hackathon
    @PostMapping("/hackathons")
    public ResponseEntity<String> createHackathon(@RequestBody HackathonCreationPayload payload) {
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

    // POST /api/organizer/hackathons/mentors -> Aggiunge un mentore all'hackathon
    @PostMapping("/hackathons/mentors")
    public ResponseEntity<String> addMentor(@RequestBody AssociationPayload payload) {
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

    // PUT /api/organizer/hackathons/winner -> Proclama il team vincitore
    @PutMapping("/hackathons/winner")
    public ResponseEntity<String> proclamaVincitore(@RequestBody AssociationPayload payload) {
        try {
            hackathonService.proclamaVincitore(
                    payload.getHackathonId(), 
                    payload.getTargetId(), 
                    payload.getOrganizerId()
            );
            return ResponseEntity.ok("🏆 Vincitore proclamato con successo!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    // DTO per la creazione dell'hackathon
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

    // DTO riutilizzabile sia per l'aggiunta del mentore che per la proclamazione del vincitore
    public static class AssociationPayload {
        private String hackathonId;
        private String targetId; // Rappresenta il mentorId o il teamId a seconda dell'endpoint
        private String organizerId;

        public String getHackathonId() { return hackathonId; }
        public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public String getOrganizerId() { return organizerId; }
        public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }
    }
}