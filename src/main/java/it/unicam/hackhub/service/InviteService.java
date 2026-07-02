package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Invite;
import it.unicam.hackhub.model.InviteState;
import it.unicam.hackhub.model.Role;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.repository.InviteRepository;
import it.unicam.hackhub.repository.UserRepository;

import java.util.UUID;

public class InviteService {

    private final InviteRepository repo;
    private final TeamService teamService;
    private final UserRepository userRepo;

    public InviteService(InviteRepository repo,
                         TeamService teamService,
                         UserRepository userRepo) {
        this.repo = repo;
        this.teamService = teamService;
        this.userRepo = userRepo;
    }

    // INVITO TEAM (firma allineata al tuo controller: leaderId, teamId, userId)
    public Invite sendInvite(String leaderId, String teamId, String userId) {

        Team team = teamService.getById(teamId);

        // Solo il leader del team può invitare
        if (team.getLeader() == null || !team.getLeader().getUserId().equals(leaderId)) {
            throw new IllegalStateException("Solo il leader del team può inviare inviti");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User non trovato"));

        if (team.isFull()) {
            throw new IllegalStateException("Team pieno");
        }

        if (teamService.isUserInAnyTeam(user.getId())) {
            throw new IllegalStateException("Utente già in un team");
        }

        boolean alreadyInvited = repo.findAll().stream()
                .anyMatch(i ->
                        i.getUser().getId().equals(userId)
                                && i.getTeam() != null
                                && i.getTeam().getId().equals(teamId)
                                && i.getState() == InviteState.PENDING
                );

        if (alreadyInvited) {
            throw new IllegalStateException("Invito già esistente");
        }

        Invite invite = new Invite(
                UUID.randomUUID().toString(),
                user,
                team
        );

        repo.save(invite);
        return invite;
    }

    public void acceptInvite(String inviteId) {
        Invite invite = repo.findById(inviteId)
                .orElseThrow(() -> new IllegalStateException("Invite non trovata"));

        Team team = invite.getTeam();
        User user = invite.getUser();

        // Qui gestiamo solo inviti team
        if (team == null) {
            throw new IllegalStateException("Invito non valido per team");
        }

        if (team.isFull()) {
            throw new IllegalStateException("Team pieno");
        }

        if (teamService.isUserInAnyTeam(user.getId())) {
            throw new IllegalStateException("Utente già in team");
        }

        invite.accept();

        team.addMember(new TeamMember(
                UUID.randomUUID().toString(),
                user,
                Role.MEMBER
        ));
    }

    public void declineInvite(String inviteId) {
        Invite invite = repo.findById(inviteId)
                .orElseThrow(() -> new IllegalStateException("Invite non trovata"));

        invite.decline();
    }

    // INVITO MENTOR (richiede model Invite con costruttore: Invite(id, user, hackathonId, "MENTOR"))
    public Invite inviteMentor(String hackathonId, String userId) {
        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalStateException("Hackathon non valido");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User non trovato"));

        boolean alreadyPending = repo.findAll().stream()
                .anyMatch(i ->
                        i.getUser().getId().equals(userId)
                                && hackathonId.equals(i.getHackathonId())
                                && "MENTOR".equals(i.getInviteType())
                                && i.getState() == InviteState.PENDING
                );

        if (alreadyPending) {
            throw new IllegalStateException("Invito mentor già esistente");
        }

        Invite invite = new Invite(
                UUID.randomUUID().toString(),
                user,
                hackathonId,
                "MENTOR"
        );

        repo.save(invite);
        return invite;
    }

    // INVITO JUDGE (richiede model Invite con costruttore: Invite(id, user, hackathonId, "JUDGE"))
    public Invite inviteJudge(String hackathonId, String userId) {
        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalStateException("Hackathon non valido");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User non trovato"));

        boolean alreadyPending = repo.findAll().stream()
                .anyMatch(i ->
                        i.getUser().getId().equals(userId)
                                && hackathonId.equals(i.getHackathonId())
                                && "JUDGE".equals(i.getInviteType())
                                && i.getState() == InviteState.PENDING
                );

        if (alreadyPending) {
            throw new IllegalStateException("Invito judge già esistente");
        }

        Invite invite = new Invite(
                UUID.randomUUID().toString(),
                user,
                hackathonId,
                "JUDGE"
        );

        repo.save(invite);
        return invite;
    }
}