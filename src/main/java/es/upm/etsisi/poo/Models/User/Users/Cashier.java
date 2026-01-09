package es.upm.etsisi.poo.Models.User.Users;

import es.upm.etsisi.poo.Models.Ticket.Ticket;
import es.upm.etsisi.poo.Models.User.UserEnums.UserType;

import java.util.HashMap;

public class Cashier extends User {

    public final HashMap<String, Ticket<?>> tickets = new HashMap<>();

    public Cashier(String id, String name, Email email) {
        super(id, name, email);
    }

    public static UserType getType() {
        return TYPE;
    }

    public int addTicket(Ticket<?> ticket) {
        if (tickets.containsKey(ticket.getTicketId())) {
            return -1; // TODO: exception
        }

        tickets.put(ticket.getTicketId(), ticket);

        return 0;
    }

    public Ticket<?> getTicket(String ticketID) {
        return tickets.get(ticketID);
    }

    private static final UserType TYPE = UserType.CASHIER;
}
