package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<StaffMember, String> {

    // I metodi save(), findById() e findAll() sono già inclusi automaticamente!

    /**
     
Recupera tutti i membri dello staff (organizzatori, giudici e mentori) 
associati a uno specifico hackathon.*/
List<StaffMember> findByHackathonId(String hackathonId);
}