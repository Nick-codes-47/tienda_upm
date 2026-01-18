package es.upm.etsisi.poo.Models.Ticket.Core;

public interface PrinterStrategy {
    void init(Ticket<?> ticket);
    String printEntry(TicketEntry<?, ?> entry);
    String printFooter();
    String printHeader();
}
