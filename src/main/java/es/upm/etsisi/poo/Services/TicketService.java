package es.upm.etsisi.poo.Services;

import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.User.Core.Cashier;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;

import java.util.ArrayList;
import java.util.List;

public class TicketService {

    private final UserRegister<Cashier> cashiers;

    public TicketService(UserRegister<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    public String getTicketList(List<Ticket<?>> tickets) {
        StringBuilder sb = new StringBuilder();
        for (Ticket<?> ticket : tickets) {
            sb.append(String.format("  %s - %s\n",
                    ticket.getID(),
                    ticket.getTicketState()));
        }
        return sb.toString();
    }

    public List<Ticket<?>> getTickets() {
        List<Ticket<?>> tickets = new ArrayList<>();

        for (Cashier cashier : cashiers) {
            tickets.addAll(cashier.tickets.values());
        }

        return tickets;
    }

    public List<Ticket<?>> getTicketsWith(BaseProduct<?> product) {
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

    public void showModifiedTickets(BaseProduct<?> product) throws AppException {
        List<Ticket<?>> tickets = this.getTicketsWith(product);
        if (!tickets.isEmpty()) {
            StringBuilder sb =
                    new StringBuilder("The tickets with the following ids had the product and it was updated:");
            for (Ticket<?> ticket : tickets) {
                ticket.update(product);
                // We show the ticket that had the product and changed
                sb.append("- ").append(ticket.getID());
            }
            AppLogger.info(sb.toString());
        }
    }

    private int totalTickets() {
        int total = 10000;
        for (Cashier cashier : cashiers)
            total += cashier.tickets.size();

        return total;
    }

    public TicketID getNewTicketID() throws InvalidAppIDException {
        return new TicketID(totalTickets() + 1);
    }
}