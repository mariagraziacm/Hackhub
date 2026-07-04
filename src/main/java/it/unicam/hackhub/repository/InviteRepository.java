package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRepository extends JpaRepository<Invite, String> {
    // save(), findById() e findAll() sono forniti nativamente da JpaRepository.
    boolean existsByUserIdAndTeamIdAndState(String userId, String teamId, Invite.InviteState state);

boolean existsByUserIdAndHackathonIdAndInviteTypeAndState(String userId, String hackathonId, Invite.InviteType inviteType, Invite.InviteState state);

}