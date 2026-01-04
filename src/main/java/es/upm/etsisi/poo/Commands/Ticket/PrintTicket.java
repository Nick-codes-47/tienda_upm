package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Ticket.Ticket;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.CashierRegister;

public class PrintTicket implements Command {
    public static final String ID = "print";

    public PrintTicket(CashierRegister cashiers) {
        this.cashiers = cashiers;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 2) {
            System.err.println("ERROR: Two arguments are required: <ticketId> <cashId>.");
            return -1;
        }

        Cashier cashier = cashiers.getUser(args[1]);
        if (cashier == null) {
            return -1; // TODO exception
        }

        Ticket ticket = cashier.getTicket(args[0]);
        if (ticket == null) {
            System.err.printf("ERROR: Cannot be found ticket with ID: '%s' or the cashier '%s' is not authorized to print it.\n", args[0], args[1]);
            return -1;
        }

        ticket.printTicket();

        return 0;
    }

    @Override
    public String help() {
        return ID + " <ticketId> <cashId>";
    }

    private final CashierRegister cashiers;
}