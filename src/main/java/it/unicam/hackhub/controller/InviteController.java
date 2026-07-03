package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Invite;
import it.unicam.hackhub.service.InviteService;

    public class InviteController {

        private final InviteService inviteService;

        public InviteController(InviteService inviteService) {
            this.inviteService = inviteService;
        }


        // leader invia invito a utente per un team
        public void sendInvite(String idLeaderUser, String idTeam, String idUserDaInvitare) {
            try {

                Invite invite = inviteService.sendInvite(idLeaderUser, idTeam, idUserDaInvitare);

                System.out.println("Invito (" + invite.getId() + ") inoltrato con successo all'utente "
                        + invite.getUser().getName());
            } catch (IllegalStateException e) {
                System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
            }
        }

        public void acceptInvite(String inviteId) {
            try {
                inviteService.acceptInvite(inviteId);
                System.out.println("Invito accettato con successo.");
            } catch (IllegalStateException e) {
                System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
            }
        }

        public void declineInvite(String inviteId) {
            try {
                inviteService.declineInvite(inviteId);
                System.out.println("SYSTEM: Invito rifiutato.");
            } catch (IllegalStateException e) {
                System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
            }
        }

        public void inviteMentor(String hackathonId, String userId) {
            try {
                Invite invite = inviteService.inviteMentor(hackathonId, userId);
                System.out.println("SYSTEM: Invito mentor creato (" + invite.getId() + ").");
            } catch (IllegalStateException e) {
                System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
            }
        }

        public void inviteJudge(String hackathonId, String userId) {
            try {
                Invite invite = inviteService.inviteJudge(hackathonId, userId);
                System.out.println("SYSTEM: Invito judge creato (" + invite.getId() + ").");
            } catch (IllegalStateException e) {
                System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
            }
        }
    }