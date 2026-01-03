package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Containers.Product.Catalog;
import es.upm.etsisi.poo.Containers.Product.ProductTypes.BaseProduct;
import es.upm.etsisi.poo.Containers.Ticket.Ticket;
import es.upm.etsisi.poo.Containers.User.Cashier;
import es.upm.etsisi.poo.Containers.User.CashierRegister;

import java.util.ArrayList;

public class AddProductToTicket implements Command {
    public static final String ID = "add";

    public AddProductToTicket(Catalog catalog, CashierRegister cashiers) {
        this.catalog = catalog;
        this.cashiers = cashiers;
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
            product = catalog.getProduct(productId);
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
        ArrayList<String> personalizations = null;

        if (numPersonalizations > 0)
            personalizations = new ArrayList<>(numPersonalizations);

        for (int i = 4; i < args.length; i++) {
            personalizations.add(args[i].replace("--p", ""));
        }

        Cashier cashier = (Cashier) cashiers.getUser(cashId); // TODO remove cast
        if (cashier == null) {
            return -1; // TODO exception
        }
        Ticket ticket = cashier.getTicket(ticketId);
        if (ticket == null) {
            return -1; // TODO exception
        }

        int result = ticket.addProduct(product, amount, personalizations);

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
        } else if (result != 0){
            System.err.println("ERROR: Unknown error occurred during product addition.\n");
        }
        return result;
    }

    @Override
    public String help() {
        return ID + " <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]";
    }

    private final Catalog catalog;
    private final CashierRegister cashiers;
}