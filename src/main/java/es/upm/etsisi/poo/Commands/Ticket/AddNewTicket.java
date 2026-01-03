package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Containers.User.Cashier;
import es.upm.etsisi.poo.Containers.User.CashierRegister;
import es.upm.etsisi.poo.Containers.User.Customer;
import es.upm.etsisi.poo.Containers.User.CustomerRegister;

public class AddNewTicket implements Command {
    public static final String ID = "new";

    public AddNewTicket(CashierRegister cashiers, CustomerRegister customers) {
        this.cashiers = cashiers;
        this.customers = customers;
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

        if(ticketId != null){ // TODO generate new
            if (!ticketId.matches("\\d+")) {
                System.err.println("ERROR: Ticket ID must be numeric.");
                return -1;
            }

            if (ticketId.length() != 5) {
                System.err.println("ERROR: Ticket ID has to be 5 digits.");
                return -1;
            }
        }

        Cashier cashier = cashiers.getUser(cashId); //TODO cast should not be necessary
        if (cashier == null) {
            System.err.println("ERROR: Cashier with ID '" + cashId + "' not found.");
            return -1;
        }

        Customer customer = customers.getUser(cashId); //TODO cast should not be necessary
        if (customer == null) {
            System.err.println("ERROR: Customer with ID '" + customerId + "' not found.");
            return -1;
        }

        int result = cashier.addTicket(ticketId, customer);
        if (result == -1) {
            System.err.println("ERROR: Ticket with ID '" + ticketId + "' already exists.");
        }
        customer.addTicket(ticketId);
        return result;
    }

    @Override
    public String help() {
        return ID +" [<ticketId>] <cashId> <customerId>";
    }

    private final CashierRegister cashiers;
    private final CustomerRegister customers;
}