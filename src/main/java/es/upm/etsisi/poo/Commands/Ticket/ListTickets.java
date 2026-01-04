package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Ticket.Ticket;
import es.upm.etsisi.poo.Services.TicketService;

import java.util.List;

public class ListTickets implements Command {
    public static final String ID = "list";

    public ListTickets(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("ERROR: Command ticket list don't accept arguments.");
            return -1;
        }

        // if cashierId is null, get all the tickets
        List<Ticket> tickets = ticketService.getTickets();
        if (tickets.isEmpty()) {
            System.err.println("ERROR: No tickets found.");
            return -1;
        }

        ticketService.printTicketList(tickets);
        return 0;
    }

    @Override
    public String help() {
        return ID;
    }

    private final TicketService ticketService;
}