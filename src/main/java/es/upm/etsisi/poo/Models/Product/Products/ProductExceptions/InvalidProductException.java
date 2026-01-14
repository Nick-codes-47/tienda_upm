package es.upm.etsisi.poo.Models.Product.Products.ProductExceptions;

import es.upm.etsisi.poo.Models.Core.AppException;

public class InvalidProductException extends AppException {
    public InvalidProductException(String message) {
        super(message);
    }
}
