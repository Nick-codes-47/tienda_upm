package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.AppExceptions.InvalidCategoryException;
import es.upm.etsisi.poo.AppExceptions.InvalidDateException;
import es.upm.etsisi.poo.Models.Product.Products.Core.ServiceID;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.ProductType;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.ServiceCategory;

import java.time.LocalDateTime;

public class ServiceProduct extends BaseProduct {

    private static final long serialVersionUID = 1L;

    public ServiceProduct(ServiceID ID, LocalDateTime expirationDate, String category)
            throws InvalidCategoryException, InvalidDateException {
        super(ProductType.SERVICE);

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
        super(other);

        this.ID = other.ID;
        this.category = other.category;
        this.expirationDate = other.expirationDate;
    }

    @Override
    public ServiceID getID() {
        return ID;
    }

    @Override
    public ServiceProduct clone() {
        return new ServiceProduct(this);
    }

    private final ServiceID ID;
    private final ServiceCategory category;
    private final LocalDateTime expirationDate;
}