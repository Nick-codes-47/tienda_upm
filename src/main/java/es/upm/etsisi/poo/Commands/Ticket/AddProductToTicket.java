package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.AppExceptions.AppException;
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

        BaseProduct product;
        try {
            ProductID productId = new ProductID(prodIdStr);
            product = catalog.get(productId);
            if (product == null) {
                System.err.printf("ERROR: Product with ID '%s' not found in the Catalog.\n", prodIdStr);
                return -1;
            }
        } catch (AppException e) {
            System.err.println(e.getMessage());
            return -1;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
            if (amount <= 0) {
                System.err.println("ERROR: Amount must be a positive integer.");
                return -1;
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Amount must be a positive integer.");
            return -1;
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
        if (cashier == null) {
            return -1; // TODO exception
        }
        int result = 0;
        try {
            Ticket<?> ticket = cashier.getTicket(new TicketID(ticketId));
            if (ticket == null) {
                return -1; // TODO exception
            }
            result = addProductTmp(product, ticket);
        } catch (AppException e) {
            System.err.println(e.getMessage());
            result = -1;
        }

        if (result == -1) {
            System.err.printf("ERROR: Ticket with ID '%s' is closed.\n", ticketId);
        } else if (result == -2) {
            System.err.print("ERROR: Maximum number of products reached.\n");
        } else if (result == -3) {
            System.err.print("ERROR: Cannot add the same event/meal to the same ticket\n");
        } else if (result == -4) {
            System.err.print("ERROR: Event requires minimum time to be planned (72h for meals and 12h for meetings).\n");
        } else if (result == -5) {
            System.err.print("ERROR: Error in the number of people in event.\n");
        } else if (result == -6) {
            System.err.print("ERROR: Maximum product customizations reached.\n");
        } else if (result == -7) {
            System.err.print("ERROR: Ticket does not exist.\n");
        } else if (result == -8) {
            System.err.print("ERROR: Product does not exist.\n");
        } else if (result != 0){
            System.err.println("ERROR: Unknown error occurred during product addition.\n");
        }
        return result;
    }

    private int addProductTmp(BaseProduct product, Ticket<?> ticket) { // TODO SMELL refactor Products and TicketEntries
        if (ticket instanceof CompanyTicket companyTicket)
            return addProduct(product, companyTicket);
        if (product instanceof GoodsProduct goody) {
            CommonTicket commonTicket = (CommonTicket) ticket;
            return addProduct(goody, commonTicket);
        }

        return 0;
    }

    private <T extends BaseProduct> int addProduct(T product, Ticket<T> ticket) {
        try {
            ticket.add(product);
        } catch (AppException e) {
            System.err.println(e.getMessage());
            return 1;
        }

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