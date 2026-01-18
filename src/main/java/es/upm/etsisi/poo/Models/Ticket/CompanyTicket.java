package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;

public class CompanyTicket extends Ticket<BaseProduct<?>> {

    private static final long serialVersionUID = 1L;

    public CompanyTicket(TicketID ID) {
        super(ID);
    }

    public CompanyTicket(CompanyTicket other) {
        super(other);
    }

    @Override
    public CompanyTicket copy() {
        return new CompanyTicket(this);
    }
}