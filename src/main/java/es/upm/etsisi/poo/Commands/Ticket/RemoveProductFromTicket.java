package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.TicketNotInCashException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Ticket.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.CashierRegister;

public class RemoveProductFromTicket implements Command {
    public static final String ID = "remove";

    private final CashierRegister cashiers;

    public RemoveProductFromTicket(CashierRegister cashiers) {
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

        int result;
        Ticket<?> ticket;

        ProductID prodId = new ProductID(prodIdStr);

        Cashier cashier = cashiers.getUser(cashId);
        if (cashier == null) throw new AppEntityNotFoundException("cashier", cashId);

        ticket = cashier.getTicket(new TicketID(ticketId));
        if (ticket == null) throw new TicketNotInCashException(ticketId, cashId);

        result = ticket.delete(prodId);

        if (result == 0) {
            System.out.println(ticket);
            return 0;
        } else if (result == -1) {
            System.err.printf("ERROR: Ticket with ID '%s' is closed.\n", ticketId);
        } else {
            System.err.println("ERROR: Unknown error occurred during product removal.");
        }
        return result;
    }

    @Override
    public String help() {
        return ID +" <ticketId> <cashId> <prodId>";
    }
}