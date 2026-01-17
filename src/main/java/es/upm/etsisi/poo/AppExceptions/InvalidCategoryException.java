package es.upm.etsisi.poo.AppExceptions;

public class InvalidCategoryException extends AppException {
    public InvalidCategoryException(String invalidCategory) {
        super(invalidCategory + " is an invalid category!");
    }
}
