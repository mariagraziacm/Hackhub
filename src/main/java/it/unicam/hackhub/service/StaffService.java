package it.unicam.hackhub.service;

import it.unicam.hackhub.model.StaffMember;
import it.unicam.hackhub.model.Organizer;
import it.unicam.hackhub.model.Mentor;
import it.unicam.hackhub.model.Judge;
import it.unicam.hackhub.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) 
public class StaffService {
    private final StaffRepository staffRepo;

    public StaffService(StaffRepository staffRepo) {
        this.staffRepo = staffRepo;
    }

    public StaffMember getById(String staffId) {
        return staffRepo.findById(staffId)
                .orElseThrow(() -> new IllegalStateException("Membro dello staff non trovato"));
    }

    public Organizer getOrganizer(String staffId) {
        StaffMember member = getById(staffId);
        if (!(member instanceof Organizer)) {
            throw new IllegalStateException("L'utente non ha i permessi di Organizzatore");
        }
        return (Organizer) member;
    }

    public Mentor getMentor(String staffId) {
        StaffMember member = getById(staffId);
        if (!(member instanceof Mentor)) {
            throw new IllegalStateException("L'utente non ha i permessi di Mentore");
        }
        return (Mentor) member;
    }

    public Judge getJudge(String staffId) {
        StaffMember member = getById(staffId);
        if (!(member instanceof Judge)) {
            throw new IllegalStateException("L'utente non ha i permessi di Giudice");
        }
        return (Judge) member;
    }
}