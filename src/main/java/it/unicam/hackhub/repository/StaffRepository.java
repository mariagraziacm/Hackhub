package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.StaffMember;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffRepository {
    private final List<StaffMember> staffList = new ArrayList<>();

    public void save(StaffMember member) {
        findById(member.getId()).ifPresent(staffList::remove);
        staffList.add(member);
    }

    public Optional<StaffMember> findById(String id) {
        return staffList.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public List<StaffMember> findAll() {
        return new ArrayList<>(staffList);
    }
    
    public List<StaffMember> findByHackathonId(String hackathonId) {
        return staffList.stream()
                .filter(s -> s.getHackathonId().equals(hackathonId))
                .toList();
    }
}