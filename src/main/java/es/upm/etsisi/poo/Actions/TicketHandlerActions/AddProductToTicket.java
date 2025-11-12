package es.upm.etsisi.poo.Actions.TicketHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.Product;
import es.upm.etsisi.poo.TicketContainer.Ticket;
import es.upm.etsisi.poo.TicketContainer.TicketEntry;

import java.util.ArrayList;

public class AddProductToTicket extends Action {
    public AddProductToTicket(App app) {
        super(app);
    }

    @Override
    public int execute(String[] args) {
        return 0;
    }

    @Override
    public String help() {
        // TODO ALL
        return "ticket add <ticketId> <cashId> <prodId> <amount> [--p --p]";
    }
}