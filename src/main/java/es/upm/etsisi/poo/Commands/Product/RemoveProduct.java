package es.upm.etsisi.poo.Commands.Product;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.AppExceptions.ArgumentExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Services.TicketService;

public class RemoveProduct implements Command {
    public static final String ID = "remove";

    public RemoveProduct(Catalog catalog, TicketService ticketService) {
        this.catalog = catalog;
        this.ticketService = ticketService;
    }

    /**
     * Method that executes the action to delete a product
     * @param args the id to remove the product
     */
    @Override
    public void execute(String[] args) throws AppException {
        if (args.length != 1) throw new WrongNumberOfArgsException(this);

        ProductID ID = new ProductID(Integer.parseInt(args[0]));
        BaseProduct<?> prod = catalog.delete(ID);

        AppLogger.info(prod.toString());

        ticketService.showModifiedTickets(prod, 'd');
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
