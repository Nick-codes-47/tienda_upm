package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Containers.Ticket.Ticket;
import es.upm.etsisi.poo.Containers.User.Cashier;
import es.upm.etsisi.poo.Containers.User.CashierRegister;

public class RemoveProductFromTicket implements Command {
    public static final String ID = "remove";

    public RemoveProductFromTicket(CashierRegister cashiers) {
        this.cashiers = cashiers;
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
            System.err.printf("ERROR: Product ID '%s' is not a valid number. Must be between 10000-99999.\n", prodIdStr);
            return -4;
        }

        Cashier cashier = cashiers.getUser(cashId);
        if (cashier == null)
            return -1; // TODO exception
        Ticket ticket = cashier.getTicket(ticketId);
        int result = ticket.deleteProduct(prodId);

        if (result == 0) {
            System.out.println(ticket);
            return 0;
        } else if (result == -1) {
            System.err.printf("ERROR: Ticket with ID '%s' is closed.\n", ticketId);
        } else if (result == -2) {
            System.err.printf("ERROR: Product with ID '%s' is not in the ticket.\n", prodIdStr);
        } else if (result == -3) {
            System.err.printf("ERROR: Ticket with ID '%s' does not exist.\n", ticketId);
        } else {
            System.err.println("ERROR: Unknown error occurred during product removal.");
        }
        return result;
    }

    @Override
    public String help() {
        return ID +" <ticketId> <cashId> <prodId>";
    }

    private final CashierRegister cashiers;
}