package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Models.Core.Copyable;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ServiceID;
import es.upm.etsisi.poo.Models.Product.ProductEnums.ServiceCategory;
import es.upm.etsisi.poo.AppExceptions.InvalidProductException;
import es.upm.etsisi.poo.Models.Ticket.Core.EntryArgs;

import java.time.LocalDateTime;

public class ServiceProduct extends BaseProduct<ServiceProduct> implements Copyable<ServiceProduct> {

    private static final long serialVersionUID = 1L;

    private final ServiceID ID;
    private final ServiceCategory category;
    private final LocalDateTime expirationDate;

    public ServiceProduct(ServiceID ID, LocalDateTime expirationDate, String category) throws InvalidProductException {
        this.ID = ID;

        if  (expirationDate == null || expirationDate.isBefore(LocalDateTime.now())) {
            throw new InvalidProductException(" services expiration is null or before now.");
        }

        this.expirationDate = expirationDate;

        try {
            this.category = ServiceCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidProductException(" services invalid category provided.");
        }
    }

    public ServiceProduct(ServiceProduct other) {
        this.ID = other.ID;
        this.category = other.category;
        this.expirationDate = other.expirationDate;
    }

    @Override
    public ServiceID getID() {
        return ID;
    }

    @Override
    public ServiceEntry toTicketEntry(EntryArgs args) {
        return new ServiceEntry(this);
    }

    @Override
    public ServiceProduct copy() {
        return new ServiceProduct(this);
    }
}