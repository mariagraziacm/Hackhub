package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, String> {
    
    boolean existsByUserIdAndTeamIdAndState(String userId, String teamId, ParticipationRequest.ParticipationRequestState state);
}