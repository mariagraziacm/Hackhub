package it.unicam.hackhub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "organizers")

@PrimaryKeyJoinColumn(name = "id") 
public class Organizer extends StaffMember {

   
    public Organizer() {
        super();
    }

    public Organizer(String id, User user, String hackathonId) {
        super(id, user, hackathonId);
    }
}