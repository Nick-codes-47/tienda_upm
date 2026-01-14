package es.upm.etsisi.poo.Services;

import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Product.Products.EventProduct;
import es.upm.etsisi.poo.Services.AddToTicketStrategies.AddEventStrategy;
import es.upm.etsisi.poo.Models.Ticket.Ticket;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.CashierRegister;
import es.upm.etsisi.poo.Services.AddToTicketStrategies.AddProductStrategy;
import es.upm.etsisi.poo.Services.AddToTicketStrategies.AddToTicketStrategy;

import java.util.ArrayList;
import java.util.List;

public class TicketService {

    public TicketService(CashierRegister cashiers) {
        this.cashiers = cashiers;
    }

    public void printTicketList(List<Ticket<?>> tickets) {
        for (Ticket<?> ticket : tickets) {
            System.out.printf("  %s - %s\n",
                    ticket.getTicketId(),
                    ticket.getTicketState());
        }
    }

    public List<Ticket<?>> getTickets() {
        List<Ticket<?>> tickets = new ArrayList<>();

        for (Cashier cashier : cashiers) {
            tickets.addAll(cashier.tickets.values());
        }

        return tickets;
    }

    public List<Ticket<?>> getTicketsWith(GoodsProduct product) {
        List<Ticket<?>> tickets = new ArrayList<>();

        for (Cashier cashier : cashiers) {
            for (Ticket<?> ticket : cashier.tickets.values()) {
                if (ticket.hasProduct(product.getId())) {
                    tickets.add(ticket);
                }
            }
        }

        return tickets;
    }

    public int add(GoodsProduct product, int quantity, Ticket<?> ticket) {
        AddToTicketStrategy strategy;
        if (product instanceof EventProduct)
            strategy = new AddEventStrategy();
        else if (product instanceof Product)
            strategy = new AddProductStrategy();
        else
            return 1;

        return ticket.add(product, quantity);
    }

    private final CashierRegister cashiers;
}
