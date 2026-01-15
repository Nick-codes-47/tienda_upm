package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;

public class CompanyTicket extends Ticket<BaseProduct> {

    private static final long serialVersionUID = 1L;

    public CompanyTicket(TicketID ID) {
        super(ID);
    }

    public CompanyTicket(CompanyTicket other) {
        super(other);
    }

    @Override
    public CompanyTicket clone() {
        return new CompanyTicket(this);
    }
}