package it.unicam.hackhub.external.Calendar;

public interface CalendarService {
    List<TimeSlot> getAvailableSlots(String mentorId);
    boolean reserveSlot(String slotId, String mentorId);
    void releaseSlot(String slotId);
    void confirmSlot(String slotId);
}
