package es.upm.etsisi.poo.Models.Product.Products.Service;

import es.upm.etsisi.poo.Models.Core.Copyable;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ServiceID;
import es.upm.etsisi.poo.Models.Product.ProductEnums.ServiceCategory;
import es.upm.etsisi.poo.AppExceptions.InvalidCategoryException;
import es.upm.etsisi.poo.AppExceptions.InvalidDateException;

import java.time.LocalDate;

public class ServiceProduct extends BaseProduct<ServiceProduct> implements Copyable<ServiceProduct> {

    private static final long serialVersionUID = 1L;

    private final ServiceID ID;
    private final ServiceCategory category;
    private final LocalDate expirationDate;

    public ServiceProduct(ServiceID ID, LocalDate expirationDate, String category)
            throws InvalidCategoryException, InvalidDateException {

        this.ID = ID;

        if  (expirationDate == null || expirationDate.isBefore(LocalDate.now())) throw new InvalidDateException();
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

    public boolean hasExpired() {
        return this.expirationDate.isBefore(LocalDate.now());
    }

    @Override
    public ServiceID getID() {
        return ID;
    }

    @Override
    public ServiceEntry toTicketEntry(String[] args) {
        return new ServiceEntry(this);
    }

    @Override
    public ServiceProduct copy() {
        return new ServiceProduct(this);
    }
}