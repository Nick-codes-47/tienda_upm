package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.TicketNotInCashException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.User.Core.Cashier;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;

public class RemoveProductFromTicket implements Command {
    public static final String ID = "remove";

    private final UserRegister<Cashier> cashiers;

    public RemoveProductFromTicket(UserRegister<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    @Override
    public int execute(String[] args) throws AppException{
        if (args.length != 3) {
            throw new WrongNumberOfArgsException();
        }

        String ticketId = args[0];
        String cashId = args[1];
        String prodIdStr = args[2];

        Ticket<?> ticket;

        ProductID prodId = new ProductID(prodIdStr);

        Cashier cashier = cashiers.getUser(cashId);
        if (cashier == null) throw new AppEntityNotFoundException("cashier", cashId);

        ticket = cashier.getTicket(new TicketID(ticketId));
        if (ticket == null) throw new TicketNotInCashException(ticketId, cashId);

        ticket.delete(prodId);

        AppLogger.info(ticket.toString());

        return 0;
    }

    @Override
    public String help() {
        return ID +" <ticketId> <cashId> <prodId>";
    }
}