package es.upm.etsisi.poo.AppExceptions;

import es.upm.etsisi.poo.Models.Product.ProductEnums.Category;

public class InvalidCategoryException extends AppException {
    public InvalidCategoryException(String invalidCategory) {
        super(invalidCategory.toString() + " is an invalid category!" + "\n" +
                "Valid categories:" + "\n" +
                Category.getCategories());
    }
}
