package es.upm.etsisi.poo.AppExceptions;

public class InvalidFieldException extends AppException {
    public InvalidFieldException() {
        super("Field not valid for this product! (remember Events don't have category)");
    }
}
