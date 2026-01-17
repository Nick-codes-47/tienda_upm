package es.upm.etsisi.poo.AppExceptions;

public class ExpiredServiceException extends ExpiredException {
    public ExpiredServiceException(String id) {
        super("Service", id);
    }
}
