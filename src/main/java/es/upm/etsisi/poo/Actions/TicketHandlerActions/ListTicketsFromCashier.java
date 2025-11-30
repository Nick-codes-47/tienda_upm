package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.TicketContainer.TicketEntry;

import java.util.List;

public class ListTicketsFromCashier extends ListTickets {
    public static final String ID = "tickets";

    public ListTicketsFromCashier() {
        super();
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 1) {
            System.err.println("ERROR: Wrong number of input args.");
            return 1;
        }

        String cashierId = args[0];
        if (App.getInstance().cashiers.getUser(cashierId) == null) {
            System.err.printf("Error: cashier {%s} does not exist\n", cashierId);
            return 2;
        }

        List<TicketEntry> tickets = getCashierTickets(cashierId);
        if (tickets == null || tickets.isEmpty()) {
            System.err.println("No tickets found.");
            return -2;
        }

        printTickets(tickets);
        return 0;
    }

    @Override
    public String help() {
        return ID + " <id>";
    }
}
