package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;

public class AddNewTicket implements Action {
    public static final String ID = "new";

    public AddNewTicket() {
    }

    @Override
    public int execute(String[] args) {
        String ticketId = null;
        String cashId;
        String customerId;

        if (args.length == 2) {
            cashId = args[0];
            customerId = args[1];
        } else if (args.length == 3) {
            ticketId = args[0];
            cashId = args[1];
            customerId = args[2];
        } else {
            System.err.println("ERROR: Two or three arguments are required: [<ticketId>] <cashId> <customerId>.");
            return -1;
        }

        if(ticketId != null){
            if (!ticketId.matches("\\d+")) {
                System.err.println("ERROR: Ticket ID must be numeric.");
                return -2;
            }

            if (ticketId.length() != 5) {
                System.err.println("ERROR: Ticket ID has to be 5 digits.");
                return -3;
            }
        }

        App app = App.getInstance();
        if (app.cashiers.getUser(cashId) == null) {
            System.err.println("ERROR: Cashier with ID '" + cashId + "' not found.");
            return -4;
        }

        if (app.customers.getUser(customerId) == null) {
            System.err.println("ERROR: Customer with ID '" + customerId + "' not found.");
            return -5;
        }

        int result = app.tickets.addNewTicket(ticketId, cashId, customerId);

        if (result == 0) {
            return 0;
        } else if (result == -1) {
            System.err.println("ERROR: Ticket with ID '" + ticketId + "' already exists.");
            return -6;
        } else {
            System.err.println("ERROR: Unknown error occurred.");
            return -7;
        }
    }

    @Override
    public String help() {
        return ID +" [<ticketId>] <cashId> <customerId>";
    }
}