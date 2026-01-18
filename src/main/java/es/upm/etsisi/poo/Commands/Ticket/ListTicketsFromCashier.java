package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.EntityExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.ContainerExceptions.EmptyContainerException;
import es.upm.etsisi.poo.AppExceptions.ArgumentExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.User.Core.Cashier;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;
import es.upm.etsisi.poo.Services.TicketService;

public class ListTicketsFromCashier implements Command {
    public static final String ID = "tickets";

    private final UserRegister<Cashier> cashiers;
    private final TicketService ticketService;

    public ListTicketsFromCashier(UserRegister<Cashier> cashiers, TicketService ticketService) {
        this.cashiers = cashiers;
        this.ticketService = ticketService;
    }

    @Override
    public void execute(String[] args) throws AppException {
        if (args.length != 1) {
            throw new WrongNumberOfArgsException(this);
        }

        String cashId = args[0];
        Cashier cashier = cashiers.getUser(cashId);
        if (cashier == null) throw new AppEntityNotFoundException("cashier", cashId);

        if (cashier.tickets.isEmpty()) throw new EmptyContainerException("tickets");

        AppLogger.info("Tickets:\n" +
                ticketService.getTicketList(cashier.tickets.values().stream().toList()));
    }

    @Override
    public String help() {
        return ID + " <id>";
    }
}
