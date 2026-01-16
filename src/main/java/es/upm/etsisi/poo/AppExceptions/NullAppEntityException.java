package es.upm.etsisi.poo.AppExceptions;

public class NullAppEntityException extends AppException {
    public NullAppEntityException(String type) {
        super(type + " is null");
    }
}
