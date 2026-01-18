package es.upm.etsisi.poo.Models.Ticket.Core;

public interface PrinterStrategy {
    abstract public void init(Ticket<?> ticket);
    abstract public String printEntry(TicketEntry<?, ?> entry);
    abstract public String printFooter();
}
