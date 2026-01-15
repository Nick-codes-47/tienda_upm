package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;

public class CommonTicket extends Ticket<GoodsProduct> {

    public CommonTicket(TicketID ID) {
        super(ID);
    }

    public CommonTicket(CommonTicket other) {
        super(other);
    }

    @Override
    public CommonTicket clone() {
        return new CommonTicket(this);
    }
}
