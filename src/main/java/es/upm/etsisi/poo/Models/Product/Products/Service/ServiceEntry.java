package es.upm.etsisi.poo.Models.Product.Products.Service;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.EntityAlreadyExistsException;
import es.upm.etsisi.poo.AppExceptions.ExpiredServiceException;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;

public class ServiceEntry extends TicketEntry<ServiceProduct, ServiceEntry> {

    private static final long serialVersionUID = 1L;

    public ServiceEntry(ServiceProduct service) {
        super(service);
    }

    @Override
    public String toString() {
        return "";
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
    public void checkValidity() throws AppException {
        if (product.hasExpired())
            throw new ExpiredServiceException(product.getID().toString());
    }

    @Override
    public void accumulate(ServiceEntry more) throws EntityAlreadyExistsException {
        throw new EntityAlreadyExistsException(
                product.getCategory().toString() + "service",
                product.getID().toString(),
                "you can't add an event twice to the same ticket");
    }
}