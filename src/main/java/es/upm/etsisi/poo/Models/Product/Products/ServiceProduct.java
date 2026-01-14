package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Models.Core.AppEntity;
import es.upm.etsisi.poo.Models.Core.AppException;
import es.upm.etsisi.poo.Models.Core.AppID;
import es.upm.etsisi.poo.Models.Product.Products.Core.ServiceID;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.ProductType;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.ServiceCategory;

import java.time.LocalDateTime;

public class ServiceProduct extends BaseProduct {

    public ServiceProduct(ServiceID ID, LocalDateTime expirationDate, String category) throws AppException {
        super(ProductType.SERVICE);

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
        super(other);

        this.ID = other.ID;
        this.category = other.category;
        this.expirationDate = other.expirationDate;
    }

    @Override
    public AppID getID() {
        return ID;
    }

    @Override
    public AppEntity clone() {
        return new ServiceProduct(this);
    }

    private final ServiceID ID;
    private final ServiceCategory category;
    private final LocalDateTime expirationDate;
}
