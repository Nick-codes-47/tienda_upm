package es.upm.etsisi.poo.Commands.Product;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Ticket.Ticket;
import es.upm.etsisi.poo.Services.TicketService;

import java.util.List;

public class RemoveProduct implements Command {
    public static final String ID = "remove";

    public RemoveProduct(Catalog catalog, TicketService ticketService) {
        this.catalog = catalog;
        this.ticketService = ticketService;
    }

    /**
     * Method that executes the action to delete a product
     * @param args the id to remove the product
     * @return 0 if all went fine
     *         1 if the id is not valid
     *         4 if the product couldn't be removed
     */
    @Override
    public int execute(String[] args) throws AppException {
        if (args.length != 1) throw new WrongNumberOfArgsException();

        ProductID ID = new ProductID(Integer.parseInt(args[0]));
        BaseProduct prod = catalog.delete(ID);

        AppLogger.info(prod.toString());

        ticketService.showModifiedTickets(prod);

        return 0;

    }

    /**
     * Shows how to call the action to remove a product
     *
     * @return a string with the command and its arguments
     */
    @Override
    public String help() {
        return ID + " <id>";
    }

    private final Catalog catalog;
    private final TicketService ticketService;
}
