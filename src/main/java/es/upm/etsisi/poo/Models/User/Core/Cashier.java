package es.upm.etsisi.poo.Models.User.Core;

import es.upm.etsisi.poo.AppExceptions.EntityExceptions.EntityAlreadyExistsException;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;

import java.util.HashMap;

public class Cashier extends User {

    private static final long serialVersionUID = 1L;

    public final HashMap<TicketID, Ticket<?>> tickets = new HashMap<>();

    public Cashier(CashID id, String name, Email email) {
        super(id.toString(), name, email);
    }

    public void addTicket(Ticket<?> ticket) throws EntityAlreadyExistsException{
        if (tickets.containsKey(ticket.getID()))
            throw new EntityAlreadyExistsException("ticket", ticket.getID().toString());

        tickets.put(ticket.getID(), ticket);
    }

    public Ticket<?> getTicket(TicketID ticketID) {
        return tickets.get(ticketID);
    }

    @Override
    public String toString() {
        return String.format(
                "Cashier{identifier='%s', name='%s', email:'%s'}", id, name, email);
    }
}