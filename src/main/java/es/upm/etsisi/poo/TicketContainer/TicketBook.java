package es.upm.etsisi.poo.TicketContainer;

import java.util.HashMap;

public class TicketBook {
    private HashMap<Integer, TicketEntry> tickets;
    private HashMap<String, String[]> userToTicket;

    public TicketBook() {
        this.tickets = new HashMap<>();
        this.userToTicket = new HashMap<>();
    }

    public int add(Ticket ticket) {
        // TODO ALL
        return 0;
    }

    public Ticket getTicket(int id) {
        // TODO ALL
        return null;
    }

    public HashMap<Integer, TicketEntry> getTickets() {
        return tickets;
    }

    public HashMap<Integer, TicketEntry> getTicketsFromUsers(String userId) {
        // TODO ALL
        return null;
    }
}
