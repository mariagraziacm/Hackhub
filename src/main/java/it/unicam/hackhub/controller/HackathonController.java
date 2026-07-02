package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.service.HackathonService;

public class HackathonController {
    private final HackathonService hackathonService;

    public HackathonController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    public void newHackathon(String id, String name, String specifications) {
        try {
            Hackathon nuovoHackathon = hackathonService.createHackathon(id, name, specifications);
            System.out.println("SYSTEM: Hackathon '" + name + "' creato con successo e impostato in fase '"
                    + nuovoHackathon.getState().getName()+ "'.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }
} //CIAO 