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
        return "ticket add <ticketId> <cashId> <prodId> <amount> [--p --p]\n" +
                "\t-\t--p For every personalization\n" +
                "\t-\tA personalizable product can be bought without being personalized\n" +
                "\t-\tAmount is the number of products to be added for normal products and\n" +
                "\t\t    personalizable ones, also is the amount of people for meetings\n" +
                "\t-\tCannot be added more than once the same meeting in the same product\n";
    }
}