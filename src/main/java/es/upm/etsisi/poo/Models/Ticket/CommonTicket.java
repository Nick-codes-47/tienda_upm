package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Ticket.Core.Printable;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;

public class CommonTicket extends Ticket<GoodsProduct<?>> implements Printable {

    private static final long serialVersionUID = 1L;

    public CommonTicket(TicketID ID) {
        super(ID);
    }

    public CommonTicket(CommonTicket other) {
        super(other);
    }

    @Override
    public CommonTicket copy() {
        return new CommonTicket(this);
    }

    @Override
    public void print() {

    };
}