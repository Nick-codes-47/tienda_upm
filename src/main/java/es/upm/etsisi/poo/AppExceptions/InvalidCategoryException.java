package es.upm.etsisi.poo.AppExceptions;

import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.Category;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.ServiceCategory;

public class InvalidCategoryException extends AppException {
    public InvalidCategoryException(String invalidCategory) {
        super(invalidCategory + " is an invalid category!");
    }
}
