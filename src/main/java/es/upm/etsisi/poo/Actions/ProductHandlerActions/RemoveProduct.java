package es.upm.etsisi.poo.Actions.ProductHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.BaseProduct;
import es.upm.etsisi.poo.TicketContainer.Ticket;

import java.util.ArrayList;

public class RemoveProduct implements Action {
    public static final String ID = "remove";

    public RemoveProduct() {
    }

    /**
     * Method that executes the action to delete a product
     * @param args the id to remove the product
     * @return 0 if all went fine
     *         1 if the id is not valid
     *         3 if the number of arguments is not valid
     *         4 if the product couldn't be removed
     */
    @Override
    public int execute(String[] args) {
        if (args.length != 1) {
            System.err.println("ERROR: Wrong number of arguments");
            return 3;
        }
        try {
            App app = App.getInstance();
            // We try to remove the product from the catalog
            int id = Integer.parseInt(args[0]);
            BaseProduct prod = app.catalog.deleteProduct(id);
            if (prod != null) {
                // If the product was deleted we show it
                System.out.println(prod);
            } else {
                // if the product wasn't in the catalog we show it
                System.err.println("ERROR: Product with id " + id + " does not exist!");
                return 4;
            }
            // We search for tickets with the product
            ArrayList<Ticket> openedTicketsWithProd = app.tickets.getOpenedTicketsWithProd(id);

            if (!openedTicketsWithProd.isEmpty()) {
                // We show the tickets that had the product
                System.out.println("The tickets with the following ids had the product and it was deleted:");
                for (Ticket ticket : openedTicketsWithProd) {
                    // We remove the product from each ticket
                    ticket.deleteProduct(prod.getId());

                    System.out.println("- " + ticket.getTicketId());
                }
            }
            // Since everything went well we return 0
            return 0;
        }  catch (NumberFormatException e) {
            System.err.println("ERROR: The product id must be an integer");
            return 1;
        }
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
}
