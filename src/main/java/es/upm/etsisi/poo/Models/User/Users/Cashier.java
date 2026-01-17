package es.upm.etsisi.poo.Models.User.Users;

import es.upm.etsisi.poo.AppExceptions.EntityAlreadyExistsException;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.User.UserEnums.UserType;

import java.util.HashMap;

public class Cashier extends User {

    private static final long serialVersionUID = 1L;

    public final HashMap<TicketID, Ticket<?>> tickets = new HashMap<>();

    public Cashier(String id, String name, Email email) {
        super(id, name, email);
    }

    public static UserType getType() {
        return type;
    }

    public void addTicket(Ticket<?> ticket) throws EntityAlreadyExistsException{
        if (tickets.containsKey(ticket.getID()))
            throw new EntityAlreadyExistsException("ticket", ticket.getID().toString());

        tickets.put(ticket.getID(), ticket);
    }

    public Ticket<?> getTicket(TicketID ticketID) {
        return tickets.get(ticketID);
    }

    private static final UserType type = UserType.CASHIER;
}