package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.model.Leader;

import java.util.Optional;

public class IscrizioneController {
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;

    public IscrizioneController(TeamRepository teamRepository, HackathonRepository hackathonRepository) {
        this.teamRepository = teamRepository;
        this.hackathonRepository = hackathonRepository;
    }

    public Team creaTeam(String id, String name, User creatore) {
        if (name == null || name.isBlank() || id == null || id.isBlank()) {
            System.out.println("ISCRIZIONE [ERRORE]: Dati del team non validi!");
            return null;
        }

        if (creatore == null) {
            System.out.println("ISCRIZIONE [ERRORE]: Il team deve essere creato da un utente valido!");
            return null;
        }

        if (teamRepository.isUserInAnyTeam(creatore.getid())) {
            System.out.println("ISCRIZIONE [ERRORE]: L'utente '" + creatore.getName() + "' fa già parte di un altro team!");
            return null;
        }

        if (teamRepository.existsByName(name)) {
            System.out.println("ISCRIZIONE [ERRORE]: Esiste già un team chiamato '" + name + "'!");
            return null;
        }

        Team nuovoTeam = new Team(id, name);

        TeamMember leaderMember = new TeamMember("TM-" + id, id, null, creatore);
        nuovoTeam.getMembers().add(leaderMember);

        Leader leaderDelTeam = new Leader(creatore.getName(), leaderMember);
        nuovoTeam.setLeader(leaderDelTeam);

        teamRepository.save(nuovoTeam);

        System.out.println("ISCRIZIONE: Team '" + name + "' registrato nel sistema. Leader: " + creatore.getName());
        return nuovoTeam;
    }


    public void iscriviTeamAdHackathon(String teamId, String hackathonId) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (teamOpt.isEmpty()) {
            System.out.println("ISCRIZIONE [ERRORE]: Team con ID " + teamId + " non trovato!");
            return;
        }
        Team team = teamOpt.get();

        Optional<Hackathon> hackathonOpt = hackathonRepository.findById(hackathonId);
        if(hackathonOpt.isEmpty()){
            return;
        }
        Hackathon hackathon = hackathonOpt.get();
        hackathon.iscriviTeam(team);
    }
}