package it.unicam.hackhub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "organizers")
// Mette in relazione la chiave primaria di questa tabella con quella di StaffMember
@PrimaryKeyJoinColumn(name = "id") 
public class Organizer extends StaffMember {

    // Costruttore vuoto obbligatorio per Spring Boot / JPA
    public Organizer() {
        super();
    }

    public Organizer(String id, User user, String hackathonId) {
        super(id, user, hackathonId);
    }
}