package es.upm.etsisi.poo.AppExceptions.ExpiredExceptions;

import java.time.LocalDateTime;

public class NotEnoughPlanningForEventException extends ExpiredException {
    public NotEnoughPlanningForEventException(String eventID, int requiredHours, LocalDateTime expirationDate) {
        super("Event", eventID,
                " needs at least " + requiredHours + " and expires on " + expirationDate.toLocalDate().toString());
    }
}
