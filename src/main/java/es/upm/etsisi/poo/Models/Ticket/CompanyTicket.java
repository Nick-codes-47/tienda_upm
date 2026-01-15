package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketType;

public class CompanyTicket extends Ticket<BaseProduct> {

    private static final long serialVersionUID = 1L;

    public CompanyTicket(TicketID ID) {
        super(TicketType.COMPANY, ID);
    }

    public CompanyTicket(CompanyTicket other) {
        super(other);
    }

    @Override
    public CompanyTicket clone() {
        return new CompanyTicket(this);
    }
}