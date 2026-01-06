package es.upm.etsisi.poo.Services;

import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Ticket.Ticket;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.CashierRegister;

import java.util.ArrayList;
import java.util.List;

public class TicketService {

    public TicketService(CashierRegister cashiers) {
        this.cashiers = cashiers;
    }

    public void printTicketList(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            System.out.printf("  %s - %s\n",
                    ticket.getTicketId(),
                    ticket.getTicketState());
        }
    }

    public List<Ticket> getTickets() {
        List<Ticket> tickets = new ArrayList<>();

        for (Cashier cashier : cashiers) {
            tickets.addAll(cashier.tickets.values());
        }

        return tickets;
    }

    public List<Ticket> getTicketsWith(GoodsProduct product) {
        List<Ticket> tickets = new ArrayList<>();

        for (Cashier cashier : cashiers) {
            for (Ticket ticket : cashier.tickets.values()) {
                if (ticket.hasProduct(product.getId())) {
                    tickets.add(ticket);
                }
            }
        }

        return tickets;
    }

    private final CashierRegister cashiers;
}
