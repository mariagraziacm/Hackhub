package it.unicam.hackhub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "mentors")

@PrimaryKeyJoinColumn(name = "id")
public class Mentor extends StaffMember {

    public Mentor() {
        super();
    }

    public Mentor(String id, User user, String hackathonId) {
        super(id, user, hackathonId);
    }
}