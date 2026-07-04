package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
  

    // Metodo mancante evidenziato in sfsdafdsafsadfas.webp
    Optional<Submission> findByHackathonIdAndTeamId(String hackathonId, String teamId);

    

    // save(), findAll(), findById(String) e deleteById(String) sono già inclusi automaticamente!

    /**
     
Cerca una sottomissione filtrando contemporaneamente per ID dell'hackathon e ID del team.
  Optional<Submission> findByHackathonIdAndTeamId(String hackathonId, String teamId);

    
     
Recupera tutte le sottomissioni associate a uno specifico hackathon.*/
  List<Submission> findByHackathonId(String hackathonId);

    /**
     
Verifica l'esistenza di una sottomissione per una specifica accoppiata hackathon-team.*/
  boolean existsByHackathonIdAndTeamId(String hackathonId, String teamId);
}