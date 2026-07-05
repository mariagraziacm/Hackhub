package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    
    boolean existsByNameIgnoreCase(String name);
boolean existsByMembers_User_Id(String userId);
}
