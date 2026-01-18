package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Products.Service.ServiceProduct;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;

public class ServiceTicket extends Ticket<ServiceProduct> {

    private static final long serialVersionUID = 1L;

    public ServiceTicket(TicketID ID) {
        super(ID, CommonPrinter::new);
    }

    public ServiceTicket(ServiceTicket other) {
        super(other);
    }

    @Override
    public ServiceTicket copy() {
        return new ServiceTicket(this);
    }
}
