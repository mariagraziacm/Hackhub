package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.BuilderHackathon;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.service.HackathonService;

public class HackathonController {
    private final HackathonService hackathonService;

    public HackathonController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    public void newHackathon(String id, String name, int partecipanti, String specifications) {
        try {
            Hackathon nuovoHackathon = hackathonService.createHackathon(id, name, partecipanti, specifications);
            System.out.println("SYSTEM: Hackathon '" + name + "' creato con successo e impostato in fase '"
                    + nuovoHackathon.getHackathonState().returnState() + "'.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }
}