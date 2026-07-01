package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.BuilderHackathon;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.repository.HackathonRepository;

public class HackathonController {
    private final HackathonRepository hackathonRepository;

    public HackathonController(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    public void newHackathon(String id, String name, int partecipanti, String specifications) {

        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            System.out.println("SYSTEM [ERRORE]: Uno o più campi obbligatori non sono stati compilati!");
            return;
        }

        if (partecipanti <= 0) {
            System.out.println("SYSTEM [ERRORE]: Numero di partecipanti non valido!");
            return;
        }

        if (hackathonRepository.existsByName(name)) {
            System.out.println("SYSTEM [ERRORE]: Esiste già un hackathon con il nome '" + name + "'!");
            return;
        }

        BuilderHackathon builder = new BuilderHackathon();
        Hackathon nuovoHackathon = builder
                .setData(id, name, partecipanti, specifications)
                .build();

        hackathonRepository.save(nuovoHackathon);

        System.out.println("SYSTEM: Hackathon '" + name + "' creato con successo e impostato in fase '"
                + nuovoHackathon.getHackathonState().returnState() + "'.");
    }
}