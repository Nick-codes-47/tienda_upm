package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Models.Core.AppEntity;
import es.upm.etsisi.poo.Models.Core.AppException;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.ProductType;

public abstract class BaseProduct extends AppEntity {

    public BaseProduct(ProductType type) {
        this.type = type;
    }

    public BaseProduct(BaseProduct other) {
        this.type = other.type;
    }

    public ProductType getType() { return type; }

    private final ProductType type;

    public static class InvalidProductException extends AppException {
        public InvalidProductException(String message) {
            super("Product " + message);
        }
    }
}
