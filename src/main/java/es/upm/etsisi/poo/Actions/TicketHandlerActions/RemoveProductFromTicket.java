package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.TicketContainer.TicketEntry; // Necesitas importar TicketEntry

public class RemoveProductFromTicket implements Action {
    public static final String ID = "remove";

    public RemoveProductFromTicket() {

    }

    @Override
    public int execute(String[] args) {
        if (args.length != 3) {
            System.err.println("ERROR: Three arguments are required: <ticketId> <cashId> <prodId>.");
            return -1;
        }

        String ticketId = args[0];
        String cashId = args[1];
        String prodIdStr = args[2];

        int prodId;
        try {
            prodId = Integer.parseInt(prodIdStr);
        } catch (NumberFormatException e) {
            System.err.printf("ERROR: Product ID '%s' is not a valid number.\n", prodIdStr);
            return -3;
        }

        int result = App.getInstance().tickets.removeProduct(ticketId, cashId, prodId);

        if (result == 0) {
            TicketEntry ticketEntry = App.getInstance().tickets.getTicketByTicketId(ticketId);

            if (ticketEntry != null && ticketEntry.ticket != null) {
                System.out.println(ticketEntry.ticket.toString());
            } else {
                System.out.println("Ticket modified: ok (Note: could not retrieve ticket details).");
            }

            return 0;
        } else if (result == -1) {
            System.err.printf("ERROR: Ticket with ID '%s' not found or cashier '%s' is not authorized.\n", ticketId, cashId);
            return -2;
        } else if (result == -2) {
            System.err.printf("ERROR: Product ID '%s' is not a valid number.\n", prodIdStr);
            return -3;
        } else if (result == -3) {
            System.err.printf("ERROR: Product with ID '%s' not found in the Catalog.\n", prodIdStr);
            return -4;
        } else if (result == -4) {
            System.err.printf("ERROR: Cannot remove product. Ticket '%s' is closed (invoice printed).\n", ticketId);
            return -5;
        } else if (result == -5) {
            System.err.printf("ERROR: Product '%s' not found in ticket '%s'.\n", prodIdStr, ticketId);
            return -6;
        } else {
            System.err.println("ERROR: Unknown error occurred during product removal.");
            return -99;
        }
    }

    @Override
    public String help() {
        return ID +" <ticketId> <cashId> <prodId>";
    }
}