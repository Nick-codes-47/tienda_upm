package es.upm.etsisi.poo.Models.Product.Products.Service;

import es.upm.etsisi.poo.AppExceptions.EntityExceptions.EntityAlreadyExistsException;
import es.upm.etsisi.poo.AppExceptions.ExpiredExceptions.ExpiredServiceException;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketClosingConstraint;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;

public class ServiceEntry extends TicketEntry<ServiceProduct, ServiceEntry> implements TicketClosingConstraint {

    private static final long serialVersionUID = 1L;

    public ServiceEntry(ServiceProduct service) {
        super(service);
    }

    @Override
    public int getProductCount() {
        return 1;
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public void checkValidity() throws ExpiredServiceException {
        if (product.hasExpired())
            throw new ExpiredServiceException(product.getID().toString());
    }

    @Override
    public void accumulate(ServiceEntry more) throws EntityAlreadyExistsException {
        throw new EntityAlreadyExistsException(
                product.getCategory().toString() + "service",
                product.getID().toString(),
                "you can't add a service twice to the same ticket");
    }

    @Override
    public String toString() {
        return this.product.toString();
    }
}