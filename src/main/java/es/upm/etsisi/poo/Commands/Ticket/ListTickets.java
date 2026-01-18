package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.EmptyDataException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Services.TicketService;

import java.util.List;

public class ListTickets implements Command {
    public static final String ID = "list";

    private final TicketService ticketService;

    public ListTickets(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void execute(String[] args) throws AppException {
        if (args.length != 0) throw new WrongNumberOfArgsException(this);

        // if cashierId is null, get all the tickets
        List<Ticket<?>> tickets = ticketService.getTickets();
        if (tickets.isEmpty()) throw new EmptyDataException("tickets");

        AppLogger.info(ticketService.getTicketList(tickets));
    }

    @Override
    public String help() {
        return ID;
    }
}