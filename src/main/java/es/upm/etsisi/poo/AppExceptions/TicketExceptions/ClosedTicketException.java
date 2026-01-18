package es.upm.etsisi.poo.AppExceptions.TicketExceptions;

import es.upm.etsisi.poo.AppExceptions.AppException;

public class ClosedTicketException extends AppException {
    public ClosedTicketException(String ticketID) {
        super("Ticket: " + ticketID + " is closed, you can't modify it");
    }
}
