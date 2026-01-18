package es.upm.etsisi.poo.Models.Ticket.Core;

import es.upm.etsisi.poo.AppExceptions.AppException;

public interface TicketClosingConstraint {
    void checkValidity() throws AppException;
}
