package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Containers.User.Cashier;
import es.upm.etsisi.poo.Containers.User.CashierRegister;
import es.upm.etsisi.poo.Services.TicketService;

public class ListTicketsFromCashier implements Command {
    public static final String ID = "tickets";

    public ListTicketsFromCashier(CashierRegister cashiers, TicketService ticketService) {
        this.cashiers = cashiers;
        this.ticketService = ticketService;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 1) {
            System.err.println("ERROR: Wrong number of input args.");
            return -1;
        }

        Cashier cashier = cashiers.getUser(args[0]);
        if (cashier == null) {
            System.err.printf("Error: cashier {%s} does not exist\n", args[0]);
            return -1;
        }

        System.out.println("Tickets:");
        ticketService.printTicketList(cashier.tickets.values().stream().toList());

        return 0;
    }

    @Override
    public String help() {
        return ID + " <id>";
    }

    private final CashierRegister cashiers;
    private final TicketService ticketService;
}
