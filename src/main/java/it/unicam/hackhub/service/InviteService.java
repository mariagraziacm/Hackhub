package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Invite;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Mentor;
import it.unicam.hackhub.model.Judge;
import it.unicam.hackhub.repository.InviteRepository;
import it.unicam.hackhub.repository.UserRepository;
import it.unicam.hackhub.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class InviteService {

    private final InviteRepository repo;
    private final TeamService teamService;
    private final UserRepository userRepo;
    private final StaffRepository staffRepo; 

    public InviteService(InviteRepository repo,
                         TeamService teamService,
                         UserRepository userRepo,
                         StaffRepository staffRepo) {
        this.repo = repo;
        this.teamService = teamService;
        this.userRepo = userRepo;
        this.staffRepo = staffRepo;
    }

    @Transactional
    public Invite sendInvite(String leaderId, String teamId, String userId) {
        Team team = teamService.getById(teamId);

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

        boolean alreadyInvited = repo.existsByUserIdAndTeamIdAndState(userId, teamId, Invite.InviteState.PENDING);

        if (alreadyInvited) {
            throw new IllegalStateException("Invito già esistente");
        }

        Invite invite = new Invite(UUID.randomUUID().toString(), user, team);
        repo.save(invite);
        return invite;
    }

    @Transactional
    public void acceptInvite(String inviteId) {
        Invite invite = repo.findById(inviteId)
                .orElseThrow(() -> new IllegalStateException("Invite non trovata"));

        User user = invite.getUser();

        if (invite.getTeam() != null) {
            Team team = invite.getTeam();
            if (team.isFull()) {
                throw new IllegalStateException("Team pieno");
            }
            if (teamService.isUserInAnyTeam(user.getId())) {
                throw new IllegalStateException("Utente già in team");
            }

            team.addMember(new TeamMember(
                    UUID.randomUUID().toString(),
                    user,
                    TeamMember.Role.MEMBER
            ));
            
            invite.accept();
            repo.save(invite);
        } else if (invite.getHackathonId() != null) {
            Invite.InviteType type = invite.getInviteType();
            String hackathonId = invite.getHackathonId();

            if (Invite.InviteType.MENTOR == type) {
                Mentor newMentor = new Mentor(UUID.randomUUID().toString(), user, hackathonId);
                staffRepo.save(newMentor); 
            } else if (Invite.InviteType.JUDGE == type) {
                Judge newJudge = new Judge(UUID.randomUUID().toString(), user, hackathonId);
                staffRepo.save(newJudge);
            }

            invite.accept();
            repo.save(invite); 
        }
    }

    @Transactional
    public void declineInvite(String inviteId) {
        Invite invite = repo.findById(inviteId)
                .orElseThrow(() -> new IllegalStateException("Invito non trovato"));

        invite.decline();
        repo.save(invite); 
    }

    @Transactional
    public Invite inviteMentor(String hackathonId, String userId) {
        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalStateException("Hackathon non valido");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User non trovato"));

        boolean alreadyPending = repo.existsByUserIdAndHackathonIdAndInviteTypeAndState(
                userId, hackathonId, Invite.InviteType.MENTOR, Invite.InviteState.PENDING
        );

        if (alreadyPending) {
            throw new IllegalStateException("Invito mentor già esistente");
        }

        Invite invite = new Invite(UUID.randomUUID().toString(), user, hackathonId, "MENTOR");
        repo.save(invite);
        return invite;
    }

    @Transactional
    public Invite inviteJudge(String hackathonId, String userId) {
        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalStateException("Hackathon non valido");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User non trovato"));

       
        boolean alreadyPending = repo.existsByUserIdAndHackathonIdAndInviteTypeAndState(
                userId, hackathonId, Invite.InviteType.JUDGE, Invite.InviteState.PENDING
        );

        if (alreadyPending) {
            throw new IllegalStateException("Invito judge già esistente");
        }

        Invite invite = new Invite(UUID.randomUUID().toString(), user, hackathonId, "JUDGE");
        repo.save(invite);
        return invite;
    }
}