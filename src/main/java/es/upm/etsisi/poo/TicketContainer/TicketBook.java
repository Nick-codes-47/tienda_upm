package es.upm.etsisi.poo.TicketContainer;

import es.upm.etsisi.poo.App;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketBook {
    private HashMap<Integer, TicketEntry> tickets;
    private HashMap<String, String[]> userToTicket;

    public TicketBook(App app) {
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
     * @param ticketsDelete list of tickets from which we have to delete the product
     * @param id the id of the product to delete
     */
    public void deleteProdFromTickets(ArrayList<Ticket> ticketsDelete, int id) {
        for (Ticket ticket : ticketsDelete) {
            if (!ticket.isClosed())
                // we only delete if the ticket is still open
                ticket.deleteProduct(id);
        }
    }

    /**
     * Method to obtain all the tickets that contain a product
     * @param id the id of the product
     * @return Tickets that contain the product
     */
    public ArrayList<Ticket> getTicketsWithProd(int id) {
        ArrayList<Ticket> ticketsWithProd = new ArrayList<>();
        // We look which tickets have the product and delete it from them
        for (TicketEntry ticketEntry : this.tickets.values()) {
            if (ticketEntry.getTicket().hasProduct(id))
                ticketsWithProd.add(ticketEntry.getTicket());
        }
        return ticketsWithProd;
    }
}
