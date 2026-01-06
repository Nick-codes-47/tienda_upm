package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Exceptions.InvalidProductException;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.ServiceCategory;

import java.time.LocalDateTime;

public class ServiceProduct extends BaseProduct {
    private final LocalDateTime expirationDate;
    private final ServiceCategory category;

    public ServiceProduct(int id, LocalDateTime expirationDate, String category) throws InvalidProductException {
        super(id);
        if  (expirationDate == null || expirationDate.isBefore(LocalDateTime.now())) {
            throw new InvalidProductException("ERROR: Expiration is null or before now.");
        }

        this.expirationDate = expirationDate;

        try {
            this.category = ServiceCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidProductException("ERROR: Invalid category provided.");
        }
    }
}
