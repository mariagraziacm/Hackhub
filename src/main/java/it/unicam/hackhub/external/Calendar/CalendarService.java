package it.unicam.hackhub.external.Calendar;
import java.util.List;
public interface CalendarService {
    List<TimeSlot> getAvailableSlots(String mentorId);
    boolean reserveSlot(String slotId, String mentorId);
    void releaseSlot(String slotId);
    void confirmSlot(String slotId);
}
