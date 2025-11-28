package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.TicketContainer.Ticket;

public class PrintTicket implements Action {
    public PrintTicket() {
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 2) {
            System.err.println("ERROR: Two arguments are required: <ticketId> <cashId>.");
            return -1;
        }

        String ticketId = args[0];
        String cashId = args[1];

        Ticket ticket = App.getInstance().tickets.getTicketIfCashierMatches(ticketId, cashId);

        if (ticket == null) {
            System.err.printf("ERROR: Cannot be found ticket with ID: '%s' or the cashier '%s' is not authorized to print it.\n", ticketId, cashId);
            return -2;
        }

        ticket.printTicket();

        return 0;
    }

    @Override
    public String help() {
        return "ticket print <ticketId> <cashId>: Shows the ticket related with a cashier .";
    }
}