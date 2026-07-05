package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<StaffMember, String> {

   
List<StaffMember> findByHackathonId(String hackathonId);
}