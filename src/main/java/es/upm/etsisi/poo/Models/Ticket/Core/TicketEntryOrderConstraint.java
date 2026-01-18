package es.upm.etsisi.poo.Models.Ticket.Core;

import java.util.Comparator;

public interface TicketEntryOrderConstraint {
    Comparator<TicketEntry<?, ?>> getSortFunction();
}
