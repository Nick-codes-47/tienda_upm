package es.upm.etsisi.poo.Containers.Ticket;

public class TicketEntry {
    public String cashId;
    public String customerId;
    public Ticket ticket;

    public TicketEntry(String cashId, String customerId, Ticket ticket) {
        this.cashId = cashId;
        this.customerId = customerId;
        this.ticket = ticket;
    }
}
