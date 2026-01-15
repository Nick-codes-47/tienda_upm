package es.upm.etsisi.poo.Services;

import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Models.Core.AppException;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;
import es.upm.etsisi.poo.Models.Ticket.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.CashierRegister;

import java.util.ArrayList;
import java.util.List;

public class TicketService {

    public TicketService(CashierRegister cashiers) {
        this.cashiers = cashiers;
    }

    public void printTicketList(List<Ticket<?>> tickets) {
        for (Ticket<?> ticket : tickets) {
            AppLogger.info(String.format("  %s - %s\n",
                    ticket.getID(),
                    ticket.getTicketState()));
        }
    }

    public List<Ticket<?>> getTickets() {
        List<Ticket<?>> tickets = new ArrayList<>();

        for (Cashier cashier : cashiers) {
            tickets.addAll(cashier.tickets.values());
        }

        return tickets;
    }

    public List<Ticket<?>> getTicketsWith(BaseProduct product) {
        List<Ticket<?>> tickets = new ArrayList<>();

        for (Cashier cashier : cashiers) {
            for (Ticket<?> ticket : cashier.tickets.values()) {
                if (ticket.hasProduct(product.getID())) {
                    tickets.add(ticket);
                }
            }
        }

        return tickets;
    }

    private int totalTickets() {
        int total = 10000;
        for (Cashier cashier : cashiers)
            total += cashier.tickets.size();

        return total;
    }

    public TicketID getNewTicketID() {
        try { return new TicketID(totalTickets() + 1); } catch (AppException e) {/* ignored its never gonna pass a negative ID */ }
        return null;
    }

    private final CashierRegister cashiers;
}