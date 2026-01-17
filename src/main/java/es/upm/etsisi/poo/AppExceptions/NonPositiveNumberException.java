package es.upm.etsisi.poo.AppExceptions;

public class NonPositiveNumberException extends AppException {
    public NonPositiveNumberException(String invalidArg) {
        super(invalidArg + "must be a positive integer");
    }
}
