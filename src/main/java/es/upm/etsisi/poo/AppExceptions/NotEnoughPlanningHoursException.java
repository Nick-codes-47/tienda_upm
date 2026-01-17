package es.upm.etsisi.poo.AppExceptions;

import java.time.LocalDateTime;

public class NotEnoughPlanningHoursException extends AppException {
    public NotEnoughPlanningHoursException(String eventID, int requiredHours, LocalDateTime expirationDate) {
        super("Event with id " + eventID + " needs a minimum of " + requiredHours + "hours.\n" +
                "It expires the " + expirationDate.toLocalDate().toString());
    }
}
