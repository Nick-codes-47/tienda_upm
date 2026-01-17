package es.upm.etsisi.poo.Models.Product.Products.Service;

import es.upm.etsisi.poo.Models.Core.Copyable;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ServiceID;
import es.upm.etsisi.poo.Models.Product.ProductEnums.ServiceCategory;
import es.upm.etsisi.poo.Models.Ticket.Core.EntryArgs;
import es.upm.etsisi.poo.AppExceptions.InvalidCategoryException;
import es.upm.etsisi.poo.AppExceptions.InvalidDateException;

import java.time.LocalDateTime;

public class ServiceProduct extends BaseProduct<ServiceProduct> implements Copyable<ServiceProduct> {

    private static final long serialVersionUID = 1L;

    public ServiceProduct(ServiceID ID, LocalDateTime expirationDate, String category)
            throws InvalidCategoryException, InvalidDateException {

        this.ID = ID;

        if  (expirationDate == null || expirationDate.isBefore(LocalDateTime.now())) throw new InvalidDateException();
        this.expirationDate = expirationDate;

        try {
            this.category = ServiceCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryException(category);
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

    private final ServiceID ID;
    private final ServiceCategory category;
    private final LocalDateTime expirationDate;
}