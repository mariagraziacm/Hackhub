package it.unicam.hackhub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "mentors")
// Collega la chiave primaria di questa tabella a quella della classe madre StaffMember
@PrimaryKeyJoinColumn(name = "id")
public class Mentor extends StaffMember {

    // Costruttore vuoto obbligatorio per Spring Boot / JPA
    public Mentor() {
        super();
    }

    public Mentor(String id, User user, String hackathonId) {
        super(id, user, hackathonId);
    }
}