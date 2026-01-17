package es.upm.etsisi.poo.AppExceptions;

public class FieldNotValidException extends AppException {
    public FieldNotValidException() {
        super("Field not valid for this product! (remember Events don't have category)");
    }
}
