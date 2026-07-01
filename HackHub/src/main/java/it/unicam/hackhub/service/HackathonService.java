package main.java.it.unicam.hackhub.service;

import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.BuilderHackathon;


public class HackathonService {
    private final HackathonRepository hackathonRepository;

    public HackathonService(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    public Hackathon createHackathon(String id, String name, int partecipanti, String specifications) {

        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Dati non validi");
        }
        if (partecipanti <= 0) {
            throw new IllegalArgumentException("Numero partecipanti non valido");
        }
        if (hackathonRepository.existsByName(name)) {
            throw new IllegalStateException("Hackathon già esistente");
        }
        Hackathon hackathon = new BuilderHackathon()
                .setData(id, name, partecipanti, specifications)
                .build();
        hackathonRepository.save(hackathon);
        return hackathon;
    }
    public Hackathon getById(String id) {
        return hackathonRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));
    }
    public void advanceState(String hackathonId) {
        Hackathon h = getById(hackathonId);
        h.avanzaStato();
    }
}
