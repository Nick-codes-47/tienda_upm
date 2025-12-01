package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.TicketContainer.TicketBook;
import es.upm.etsisi.poo.TicketContainer.TicketEntry;

import java.util.List;

public class ListTickets implements Action {
    public static final String ID = "list";

    public ListTickets() {
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("ERROR: Command ticket list don't accept arguments.");
            return -1;
        }

        // if cashierId is null, get all the tickets
        List<TicketEntry> tickets = getCashierTickets(null);
        if (tickets == null || tickets.isEmpty()) {
            System.err.println("ERROR: No tickets found.");
            return -1;
        }

        printTickets(tickets);
        return 0;
    }

    public void printTickets(List<TicketEntry> tickets) {
        for (TicketEntry entry : tickets) {
            System.out.printf("  %s - %s\n",
                entry.ticket.getTicketId(),
                entry.ticket.getTicketState());
        }
    }

    public List<TicketEntry> getCashierTickets(String cashierId) {
        TicketBook tickets = App.getInstance().tickets;

        if (cashierId == null) {
            return tickets.listTicketsSortedByCashierId();
        }

        return tickets.getTicketsFrom(cashierId);
    }

    @Override
    public String help() {
        return ID;
    }
}