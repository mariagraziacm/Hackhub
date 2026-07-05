package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, String> {

    
boolean existsByNameIgnoreCase(String name);
}