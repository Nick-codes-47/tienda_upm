package es.upm.etsisi.poo.TicketContainer;

import java.util.ArrayList;
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

    /**
     * Method to obtain all the tickets that had deleted a product
     * @param id the id of the product to delete
     * @return Tickets that contained the product
     */
    public ArrayList<Ticket> deleteProdFromTickets(int id) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        // We look which tickets have the product and delete it from them
        for (TicketEntry ticketEntry : this.tickets.values()) {
            int deleteFromTicket = ticketEntry.getTicket().deleteProduct(id);
            if (deleteFromTicket == 0)
                // if the ticket had the product we add it to the list
                tickets.add(ticketEntry.getTicket());
        }
        return tickets;
    }
}
