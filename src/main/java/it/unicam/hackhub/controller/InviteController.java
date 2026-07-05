package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Invite;
import it.unicam.hackhub.service.InviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invites")
public class InviteController {

    private final InviteService inviteService;

    public InviteController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    
    @PostMapping("/team")
    public ResponseEntity<String> sendInvite(@RequestBody TeamInvitePayload payload) {
        try {
            Invite invite = inviteService.sendInvite(
                    payload.getIdLeaderUser(), 
                    payload.getIdTeam(), 
                    payload.getIdUserDaInvitare()
            );
            return ResponseEntity.ok("Invito (" + invite.getId() + ") inoltrato con successo all'utente "
                    + invite.getUser().getName());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    
    @PutMapping("/{inviteId}/accept")
    public ResponseEntity<String> acceptInvite(@PathVariable String inviteId) {
        try {
            inviteService.acceptInvite(inviteId);
            return ResponseEntity.ok("Invito accettato con successo.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }


    @PutMapping("/{inviteId}/decline")
    public ResponseEntity<String> declineInvite(@PathVariable String inviteId) {
        try {
            inviteService.declineInvite(inviteId);
            return ResponseEntity.ok("SYSTEM: Invito rifiutato.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    
    @PostMapping("/mentor")
    public ResponseEntity<String> inviteMentor(@RequestBody StaffInvitePayload payload) {
        try {
            Invite invite = inviteService.inviteMentor(payload.getHackathonId(), payload.getUserId());
            return ResponseEntity.ok("SYSTEM: Invito mentor creato (" + invite.getId() + ").");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    @PostMapping("/judge")
    public ResponseEntity<String> inviteJudge(@RequestBody StaffInvitePayload payload) {
        try {
            Invite invite = inviteService.inviteJudge(payload.getHackathonId(), payload.getUserId());
            return ResponseEntity.ok("SYSTEM: Invito judge creato (" + invite.getId() + ").");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    
    public static class TeamInvitePayload {
        private String idLeaderUser;
        private String idTeam;
        private String idUserDaInvitare;

        public String getIdLeaderUser() { return idLeaderUser; }
        public void setIdLeaderUser(String idLeaderUser) { this.idLeaderUser = idLeaderUser; }
        public String getIdTeam() { return idTeam; }
        public void setIdTeam(String idTeam) { this.idTeam = idTeam; }
        public String getIdUserDaInvitare() { return idUserDaInvitare; }
        public void setIdUserDaInvitare(String idUserDaInvitare) { this.idUserDaInvitare = idUserDaInvitare; }
    }

   
    public static class StaffInvitePayload {
        private String hackathonId;
        private String userId;

        public String getHackathonId() { return hackathonId; }
        public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
}