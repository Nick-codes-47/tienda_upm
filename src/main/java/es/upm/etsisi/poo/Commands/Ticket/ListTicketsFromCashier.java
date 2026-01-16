package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.EmptyDataException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.CashierRegister;
import es.upm.etsisi.poo.Services.TicketService;

public class ListTicketsFromCashier implements Command {
    public static final String ID = "tickets";

    public ListTicketsFromCashier(CashierRegister cashiers, TicketService ticketService) {
        this.cashiers = cashiers;
        this.ticketService = ticketService;
    }

    @Override
    public int execute(String[] args) throws AppException {
        if (args.length != 1) {
            throw new WrongNumberOfArgsException();
        }

        String cashId = args[0];
        Cashier cashier = cashiers.getUser(cashId);
        if (cashier == null) throw new AppEntityNotFoundException("cashier", cashId);

        if (cashier.tickets.isEmpty()) throw new EmptyDataException("tickets");

        AppLogger.info("Tickets:\n" + ticketService.getTicketList(cashier.tickets.values().stream().toList()));

        return 0;
    }

    @Override
    public String help() {
        return ID + " <id>";
    }

    private final CashierRegister cashiers;
    private final TicketService ticketService;
}
