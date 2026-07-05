package it.unicam.hackhub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "judges")

@PrimaryKeyJoinColumn(name = "id")
public class Judge extends StaffMember {

    public Judge() {
        super();
    }

    public Judge(String id, User user, String hackathonId) {
        super(id, user, hackathonId);
    }
}