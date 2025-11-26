package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;

import java.util.ArrayList;

public class AddProductToTicket extends Action {
    public AddProductToTicket(App app) {
        super(app);
    }

    @Override
    public int execute(String[] args) {
        // Formato: <ticketId> <cashId> <prodId> <amount> [--p ...]
        if (args.length < 4) {
            System.err.println("ERROR: At least four arguments are required: <ticketId> <cashId> <prodId> <amount> [personalizations].");
            return -1;
        }

        String ticketId = args[0];
        String cashId = args[1];
        String prodIdStr = args[2];
        String amountStr = args[3];

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
            if (amount <= 0) {
                System.err.println("ERROR: Amount must be a positive integer.");
                return -2;
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Amount must be an integer.");
            return -2;
        }

        ArrayList<String> personalizations = new ArrayList<>();
        for (int i = 4; i < args.length; i++) {
            personalizations.add(args[i]);
        }

        int result = app.tickets.addProductToTicket(ticketId, cashId, prodIdStr, amount, personalizations);

        if (result == 0) {
            System.out.printf("Product '%s' (x%d) added successfully to ticket '%s'.\n", prodIdStr, amount, ticketId);
            if (!personalizations.isEmpty()) {
                System.out.printf("Personalizations applied: %s\n", personalizations);
            }
            return 0;
        } else if (result == -1) {
            System.err.printf("ERROR: Ticket with ID '%s' not found or cashier '%s' is not authorized.\n", ticketId, cashId);
            return -3;
        } else if (result == -2) {
            System.err.printf("ERROR: Product ID '%s' is not a valid number.\n", prodIdStr);
            return -4;
        } else if (result == -3) {
            System.err.printf("ERROR: Product with ID '%s' not found in the Catalog.\n", prodIdStr);
            return -5;
        } else if (result == -4) {
            System.err.printf("ERROR: Cannot add product. Ticket '%s' is closed (invoice printed).\n", ticketId);
            return -6;
        } else if (result == -5) {
            System.err.printf("ERROR: Maximum number of items reached in ticket '%s'.\n", ticketId);
            return -7;
        } else if (result == -6) {
            System.err.printf("ERROR: Event '%s' cannot be added. Requires longer planning time.\n", prodIdStr);
            return -8;
        } else if (result == -7) {
            System.err.printf("ERROR: Event (meeting/meal) '%s' is already in ticket '%s' and cannot be added again.\n", prodIdStr, ticketId);
            return -9;
        } else {
            System.err.println("ERROR: Unknown error occurred during product addition.");
            return -99;
        }
    }

    @Override
    public String help() {
        return "ticket add <ticketId> <cashId> <prodId> <amount> [--p --p]: Adds a product to an open ticket.\n" +
                "\t- [--p --p] are optional personalizations for custom products.\n" +
                "\t- Amount is units for products or people for events/meals.\n" +
                "\t- Events (meetings/meals) cannot be added if already present in the ticket.";
    }
}