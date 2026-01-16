package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Ticket.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.CashierRegister;

public class PrintTicket implements Command {
    public static final String ID = "print";

    public PrintTicket(CashierRegister cashiers) {
        this.cashiers = cashiers;
    }

    @Override
    public int execute(String[] args)
            throws WrongNumberOfArgsException, AppEntityNotFoundException, InvalidAppIDException {
        if (args.length != 2) throw new WrongNumberOfArgsException();

        String cashId = args[1];
        Cashier cashier = cashiers.getUser(cashId);
        if (cashier == null) throw new AppEntityNotFoundException("cashier", cashId);

        String ticketId = args[0];
        Ticket<?> ticket = cashier.getTicket(new TicketID(ticketId));
        if (ticket == null) throw new TicketNotInCashException(ticketId, cashId);

        ticket.print();

        return 0;
    }

    @Override
    public String help() {
        return ID + " <ticketId> <cashId>";
    }

    private final CashierRegister cashiers;
}