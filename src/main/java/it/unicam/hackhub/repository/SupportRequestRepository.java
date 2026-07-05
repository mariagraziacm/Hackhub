package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.SupportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SupportRequestRepository extends JpaRepository<SupportRequest, String> {

List<SupportRequest> findByMentorId(String mentorId);
}