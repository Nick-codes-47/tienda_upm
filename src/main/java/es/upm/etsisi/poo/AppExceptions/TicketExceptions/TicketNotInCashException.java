package es.upm.etsisi.poo.AppExceptions.TicketExceptions;

import es.upm.etsisi.poo.AppExceptions.EntityExceptions.AppEntityNotFoundException;

public class TicketNotInCashException extends AppEntityNotFoundException {
    private final String cashId;

    public TicketNotInCashException(String ticketId, String cashId) {
        super("ticket", ticketId);

        this.cashId = cashId;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " in cashier with id " + cashId;
    }
}
