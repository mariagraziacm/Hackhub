package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.model.Invite;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.InviteState;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InviteController {
    private final TeamRepository teamRepository;
    private final it.unicam.hackhub.repository.UserRepository userRepository;
    private final List<Invite> invites = new ArrayList<>();

    public InviteController(TeamRepository teamRepository, it.unicam.hackhub.repository.UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public void sendInvite(String idLeaderUser, String idTeam, String idUserDaInvitare) {
        Optional<Team> teamOpt = teamRepository.findById(idTeam);
        if (teamOpt.isEmpty()) {
            System.out.println("SYSTEM [ERRORE]: Team non trovato!");
            return;
        }
        Team team = teamOpt.get();

        if (team.getLeader() == null || !team.getLeader().getUser().getId().equals(idLeaderUser)) {
            System.out.println("SYSTEM [ERRORE]: Solo il leader del team può inviare inviti!");
            return;
        }
        if (team.isFull()) {
            System.out.println("SYSTEM [ERRORE]: Impossibile invitare altri utenti, il team è al completo!");
            return;
        }

        Optional<User> userOpt = userRepository.findById(idUserDaInvitare);
        if (userOpt.isEmpty()) {
            System.out.println("SYSTEM [ERRORE]: L'utente selezionato non esiste!");
            return;
        }
        User utenteDaInvitare = userOpt.get();

        if (teamRepository.isUserInAnyTeam(utenteDaInvitare.getId())) {
            System.out.println("SYSTEM [ERRORE]: L'utente selezionato fa già parte di un altro team!");
            return;
        }

        String inviteId = "INV-" + (invites.size() + 1);
        Invite nuovoInvito = new Invite(inviteId, utenteDaInvitare, team);
        invites.add(nuovoInvito);

        System.out.println("SYSTEM: Invito (" + inviteId + ") inoltrato con successo all'utente " + utenteDaInvitare.getName());
    }
}