package es.upm.etsisi.poo.Actions.ProductHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.BaseProduct;
import es.upm.etsisi.poo.TicketContainer.Ticket;
import es.upm.etsisi.poo.TicketContainer.TicketEntry;

import java.util.ArrayList;

public class RemoveProduct extends Action {
    public RemoveProduct(App app) {
        super(app);
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
            ArrayList<Ticket> tickets = app.tickets.getTicketsWithProd(id);
            boolean anyOpen = false;
            if (!tickets.isEmpty()) {
                // We delete the product from the tickets
                app.tickets.deleteProdFromTickets(tickets, id);
                // We show the tickets that had the product
                for (Ticket ticket : tickets) {
                    if (!ticket.isClosed()) {
                        if (!anyOpen) {
                            System.out.println("The tickets with the following ids had the product and it was deleted:");
                            anyOpen = true;
                        }
                    }
                    System.out.println("- "+ticket.getId());
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
     */
    @Override
    public void help() {
        System.out.println("prod remove <id>");
    }
}
