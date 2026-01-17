package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketRegistrable;
import es.upm.etsisi.poo.Models.User.Core.Cashier;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;

import java.util.Arrays;

public class AddProductToTicket implements Command {
    public static final String ID = "add";

    private final Catalog catalog;
    private final UserRegister<Cashier> cashiers;

    public AddProductToTicket(Catalog catalog, UserRegister<Cashier> cashiers) {
        this.catalog = catalog;
        this.cashiers = cashiers;
    }

    @Override
    public int execute(String[] rawArgs) throws AppException {
        CommandArgs args = new CommandArgs(rawArgs);

        BaseProduct<?> product = catalog.get(args.productID);
        if (product == null) throw new AppEntityNotFoundException("product", args.productID.toString());

        Cashier cashier = cashiers.getUser(args.cashID);
        if (cashier == null) throw new AppEntityNotFoundException("cashier", args.cashID);

        Ticket<?> ticket = cashier.getTicket(args.ticketID);
        if (ticket == null) throw new TicketNotInCashException(args.ticketID.toString(), args.cashID);

        int result = 1;
        try {
            result = addToTicket(ticket, product, args.args);
        } catch (ClassCastException e) {
            AppLogger.warn(String.format("Product type %s, can not be added to a %s", product.getClass(), ticket.getClass()));
        }

        return result;
    }

    // If the product is not the correct type, we expect the ClassCastException to be thrown
    @SuppressWarnings("unchecked")
    private <T extends BaseProduct<?>> int addToTicket(Ticket<T> ticket, BaseProduct<?> baseProduct, String[] args)
            throws AppException, ClassCastException {
        return ticket.add((TicketRegistrable<T>) baseProduct, args);
    }

    private static class CommandArgs {
        TicketID ticketID;
        String cashID;
        ProductID productID;
        String[] args = null;

        public CommandArgs(String[] args) throws InvalidAppIDException, NonPositiveNumberException, WrongNumberOfArgsException {
            if (args.length < 3) {
                throw new WrongNumberOfArgsException();
            }

            String ticketId = args[0];
            String cashId = args[1];
            String prodIdStr = args[2];

            this.productID = new ProductID(prodIdStr);
            this.ticketID = new TicketID(ticketId);
            this.cashID = cashId;

            if (args.length > 3)
                this.args = Arrays.copyOfRange(args, 3, args.length);
        }
    }

    @Override
    public String help() {
        return ID + " <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]";
    }
}