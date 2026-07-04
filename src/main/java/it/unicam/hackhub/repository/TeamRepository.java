package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    // save(), findById() e findAll() sono già inclusi automaticamente!

     
/*Verifica se esiste un team con il nome specificato (case-insensitive).
Spring converte automaticamente questo metodo in una query SQL con clausola 'UPPER' o 'LOWER
boolean existsByNameIgnoreCase(String name);

    /
     
Naviga le relazioni: controlla se nella tabella dei membri del team (members)
esiste un utente (user) con lo specifico ID (id).
Sostituisce in modo super efficiente il vecchio flatMap in memoria*/

    boolean existsByNameIgnoreCase(String name);
boolean existsByMembers_User_Id(String userId);
}
