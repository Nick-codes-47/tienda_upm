package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Products.Service.ServiceProduct;
import es.upm.etsisi.poo.Models.Ticket.Core.Printable;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;

public class ServiceTicket extends Ticket<ServiceProduct> implements Printable {

    private static final long serialVersionUID = 1L;

    public ServiceTicket(TicketID ID) {
        super(ID);
    }

    public ServiceTicket(ServiceTicket other) {
        super(other);
    }

    @Override
    public ServiceTicket copy() {
        return new ServiceTicket(this);
    }

    @Override
    public void print() {
    }
}
