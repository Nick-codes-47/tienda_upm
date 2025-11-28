package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;

public class RemoveProductFromTicket extends Action {
    public RemoveProductFromTicket(App app) {
        super(app);
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

        int result = app.tickets.removeProduct(ticketId, cashId, Integer.parseInt(prodIdStr));

        if (result == 0) {
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
        return "ticket remove <ticketId> <cashId> <prodId>: Removes a product from a specific open ticket, if the cashier is authorized.";
    }
}