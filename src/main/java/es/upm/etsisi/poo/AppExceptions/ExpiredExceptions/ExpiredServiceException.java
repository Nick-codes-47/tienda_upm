package es.upm.etsisi.poo.AppExceptions.ExpiredExceptions;

public class ExpiredServiceException extends ExpiredException {
    public ExpiredServiceException(String id) {
        super("Service", id);
    }
}
