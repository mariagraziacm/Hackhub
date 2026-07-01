package main.java.it.unicam.hackhub.service;

import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.BuilderHackathon;
import  it.unicam.hackhub.model.Team;

public class HackathonService{
    private final HackathonRepository hackathonRepository;

    public HackathonService(HackathonRepository hackathonRepository){
        this.hackathonRepository = hackathonRepository;
    }

    public Hackathon createHackathon(String id, String name, int partecipanti, String specifications){
        if(id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Uno o più campi obbligatori non sono stati compilati!");
        }
        if(partecipanti <= 0){
            throw new IllegalArgumentException("Numero di partecipanti non valido!");
        }
        if (hackathonRepository.existsByName(name)){
            throw new IllegalStateException("Esiste già un hackathon con il nome '" + name + "'!");
        }

        Hackathon hackathon = new BuilderHackathon()
                .setData(id, name, partecipanti, specifications)
                .build();

        hackathonRepository.save(hackathon);
        return hackathon;
    }

    public void iscriviTeam(Team team, String hackathonId){
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        if(team.getMembers().size() < 2){
            throw new IllegalStateException("Il team '" + team.getName() + "' deve avere almeno 2 membri per potersi iscrivere!");
        }

        hackathon.iscriviTeam(team);
    }

    public void disiscriviTeam(Team team, String hackathonId){
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        hackathon.disiscriviTeam(team);
    }

    public Hackathon getById(String id){
        return hackathonRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));
    }
}