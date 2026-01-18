package es.upm.etsisi.poo.AppExceptions;

public class NonPositiveIntegerException extends AppException {
    public NonPositiveIntegerException(String invalidArg) {
        super(invalidArg + "must be a positive integer");
    }
}
