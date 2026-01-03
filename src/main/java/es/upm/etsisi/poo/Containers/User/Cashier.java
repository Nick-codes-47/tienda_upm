package es.upm.etsisi.poo.Containers.User;

import es.upm.etsisi.poo.Containers.Ticket.Ticket;

import java.util.HashMap;

public class Cashier extends User {

    public final HashMap<String, Ticket> tickets = new HashMap<>();

    public Cashier(String id, String name, Email email) {
        super(id, name, email);
    }

    public static String getType() {
        return TYPE;
    }

    public int addTicket(String ticketID, User customer) {
        if (tickets.containsKey(ticketID)) {
            return -1; // TODO: exception
        }

        Ticket ticket = new Ticket(ticketID);
        tickets.put(ticketID, ticket);

        return 0;
    }

    public Ticket getTicket(String ticketID) {
        return tickets.get(ticketID);
    }

    private static final String TYPE = "Cash";
}
