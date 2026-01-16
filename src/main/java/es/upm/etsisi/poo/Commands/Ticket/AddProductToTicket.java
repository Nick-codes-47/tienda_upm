package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Ticket.CommonTicket;
import es.upm.etsisi.poo.Models.Ticket.CompanyTicket;
import es.upm.etsisi.poo.Models.Ticket.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.CashierRegister;

import java.util.ArrayList;

public class AddProductToTicket implements Command {
    public static final String ID = "add";

    private final Catalog catalog;
    private final CashierRegister cashiers;

    public AddProductToTicket(Catalog catalog, CashierRegister cashiers) {
        this.catalog = catalog;
        this.cashiers = cashiers;
    }

    @Override
    public int execute(String[] args) throws AppException {
        if (args.length < 4) {
            throw new WrongNumberOfArgsException();
        }

        String ticketId = args[0];
        String cashId = args[1];
        String prodIdStr = args[2];
        String amountStr = args[3];

        ProductID productId = new ProductID(prodIdStr);

        BaseProduct product = catalog.get(productId);

        if (product == null) throw new AppEntityNotFoundException("product", productId.toString());

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
            if (amount <= 0) throw new InvalidAmountException();
        } catch (NumberFormatException e) {
            throw new InvalidAmountException();
        }

        int numPersonalizations = args.length - 4;
        ArrayList<String> personalizations = null;

        if (numPersonalizations > 0) {
            personalizations = new ArrayList<>(numPersonalizations);

            for (int i = 4; i < args.length; i++) {
                personalizations.add(args[i].replace("--p", ""));
            }
        }

        Cashier cashier = cashiers.getUser(cashId);
        if (cashier == null) throw new AppEntityNotFoundException("cashier", cashId);

        Ticket<?> ticket = cashier.getTicket(new TicketID(ticketId));
        if (ticket == null) throw new TicketNotInCashException(ticketId, cashId);

        int result = addProductTmp(product, ticket);

        if (result == -3) {
            System.err.print("ERROR: Cannot add the same event/meal to the same ticket\n");
        } else if (result == -4) {
            System.err.print("ERROR: Event requires minimum time to be planned (72h for meals and 12h for meetings).\n");
        } else if (result == -5) {
            System.err.print("ERROR: Error in the number of people in event.\n");
        } else if (result == -6) {
            System.err.print("ERROR: Maximum product customizations reached.\n");
        }

        return result;
    }

    private int addProductTmp(BaseProduct product, Ticket<?> ticket) throws AppException { // TODO SMELL refactor Products and TicketEntries
        if (ticket instanceof CompanyTicket companyTicket)
            return addProduct(product, companyTicket);
        if (product instanceof GoodsProduct goody) {
            CommonTicket commonTicket = (CommonTicket) ticket;
            return addProduct(goody, commonTicket);
        }

        return 0;
    }

    private <T extends BaseProduct> int addProduct(T product, Ticket<T> ticket) throws AppException {
        ticket.add(product);
        return 0;
    }

    private <T extends BaseProduct, U extends BaseProduct> int add(T product, Ticket<U> ticket) {
        System.err.printf("ERROR: Can not add product type %s to a %s ticket\n", product.getType(), ticket.getType());

        return -1;
    }

    @Override
    public String help() {
        return ID + " <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]";
    }
}