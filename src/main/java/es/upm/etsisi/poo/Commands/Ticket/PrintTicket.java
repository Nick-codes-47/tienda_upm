package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.AppExceptions.ArgumentExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.AppExceptions.EntityExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.TicketExceptions.TicketNotInCashException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.User.Core.Cashier;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;
import es.upm.etsisi.poo.Services.TicketService;

public class PrintTicket implements Command {
    public static final String ID = "print";

    private final TicketService ticketService;
    private final UserRegister<Cashier> cashiers;

    public PrintTicket(TicketService ticketService, UserRegister<Cashier> cashiers) {
        this.ticketService = ticketService;
        this.cashiers = cashiers;
    }

    @Override
    public void execute(String[] args)
            throws AppException {
        if (args.length != 2) throw new WrongNumberOfArgsException(this);

        String cashId = args[1];
        Cashier cashier = cashiers.getUser(cashId);
        if (cashier == null) throw new AppEntityNotFoundException("cashier", cashId);

        String ticketId = args[0];
        Ticket<?> ticket = cashier.getTicket(new TicketID(ticketId));
        if (ticket == null) throw new TicketNotInCashException(ticketId, cashId);

        ticket.close();
        AppLogger.info(ticket.print(ticketService.getPrinter(ticket)));
    }

    @Override
    public String help() {
        return ID + " <ticketId> <cashId>";
    }
}