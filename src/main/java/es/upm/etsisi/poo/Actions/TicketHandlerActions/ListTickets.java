package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.TicketContainer.TicketEntry;

import java.util.List;

public class ListTickets extends Action {
    public ListTickets(App app) {
        super(app);
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("ERROR: Command ticket list don't accept arguments.");
            return -1;
        }

        List<TicketEntry> sortedTickets = app.tickets.listTicketsSortedByCashierId();

        if (sortedTickets.isEmpty()) {
            System.out.println("No tickets found.");
            return 0;
        }

        for (TicketEntry entry : sortedTickets) {
            System.out.printf("%s - %s\n",
                    entry.ticket.getTicketId(),
                    entry.ticket.getTicketState());
        }

        return 0;
    }

    @Override
    public String help() {
        return "ticket list: Show every ticket in the list sorted by CashID.";
    }
}