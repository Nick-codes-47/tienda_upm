package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.BaseProduct;

import java.util.ArrayList;

public class AddProductToTicket implements Action {
    public static final String ID = "add";

    public AddProductToTicket() {
    }

    @Override
    public int execute(String[] args) {
        if (args.length < 4) {
            System.err.println("ERROR: At least four arguments are required: <ticketId> <cashId> <prodId> <amount> [personalizations].");
            return -1;
        }

        String ticketId = args[0];
        String cashId = args[1];
        String prodIdStr = args[2];
        String amountStr = args[3];

        BaseProduct product;
        try {
            int productId = Integer.parseInt(prodIdStr);
            product = App.getInstance().catalog.getProduct(productId);
            if (product == null) {
                System.err.printf("ERROR: Product with ID '%s' not found in the Catalog.\n", prodIdStr);
                return -1;
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: prodId must be an integer.");
            return -1;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
            if (amount <= 0) {
                System.err.println("ERROR: Amount must be a positive integer.");
                return -1;
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Amount must be a positive integer.");
            return -1;
        }

        int numPersonalizations = args.length - 4;
        ArrayList<String> personalizations = new ArrayList<>(numPersonalizations);

        for (int i = 4; i < args.length; i++) {
            personalizations.add(args[i].replace("--p", ""));
        }

        int result = App.getInstance().tickets.addProductToTicket(ticketId, cashId, product, amount, personalizations);

        if (result == -1) {
            System.err.printf("ERROR: Ticket with ID '%s' is closed.\n", ticketId);
        } else if (result == -2) {
            System.err.print("ERROR: Maximum number of products reached.\n");
        } else if (result == -3) {
            System.err.print("ERROR: Cannot add the same event/meal to the same ticket\n");
        } else if (result == -4) {
            System.err.print("ERROR: Event requires minimum time to be planned (72h for meals and 12h for meetings).\n");
        } else if (result == -5) {
            System.err.print("ERROR: Error in the number of people in event.\n");
        } else if (result == -6) {
            System.err.print("ERROR: Maximum product customizations reached.\n");
        } else if (result == -7) {
            System.err.print("ERROR: Ticket does not exist.\n");
        } else if (result == -8) {
            System.err.print("ERROR: Product does not exist.\n");
        } else {
            System.err.println("ERROR: Unknown error occurred during product addition.");
        }
        return result;
    }

    @Override
    public String help() {
        return ID + " <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]";
    }
}