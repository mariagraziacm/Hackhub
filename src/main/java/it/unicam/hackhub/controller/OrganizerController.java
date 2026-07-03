package it.unicam.hackhub.controller;

import it.unicam.hackhub.service.HackathonService;
import it.unicam.hackhub.model.Hackathon;

public class OrganizerController {
    private final HackathonService hackathonService;

    public OrganizerController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    public void createHackathon(String id, String name, String specifications, String organizerId) {
        try {
            Hackathon h = hackathonService.createHackathon(id, name, specifications, organizerId);
            System.out.println("SYSTEM [SUCCESS]: Hackathon '" + h.getName() + "' creato dall'organizzatore.");
        } catch (Exception e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    public void addMentor(String hackathonId, String mentorId, String organizerId) {
        try {
            hackathonService.addMentorToHackathon(hackathonId, mentorId, organizerId);
            System.out.println("SYSTEM [SUCCESS]: Mentore aggiunto con successo all'hackathon.");
        } catch (Exception e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }
    public void proclamaVincitore(String hackathonId, String teamId, String organizerId) {
        try {
            hackathonService.proclamaVincitore(hackathonId, teamId, organizerId);
            System.out.println("🏆 Vincitore proclamato con successo!");
        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }

}