package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.SupportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SupportRequestRepository extends JpaRepository<SupportRequest, String> {

    // save(), findById() e findAll() sono forniti nativamente da JpaRepository.

    /**
     
Spring genera automaticamente la query SQL per filtrare 
le richieste di supporto in base all'ID del mentore.*/
List<SupportRequest> findByMentorId(String mentorId);
}