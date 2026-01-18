package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Ticket.Core.PrinterStrategy;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;

import java.io.Serializable;

public class ServicePrinter implements PrinterStrategy, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void init(Ticket<?> ticket) {

    }

    @Override
    public String printHeader() {
        return "";
    }

    @Override
    public String printEntry(TicketEntry<?, ?> entry) {
        return entry.toString();
    }

    @Override
    public String printFooter() {
        return "";
    }
}
