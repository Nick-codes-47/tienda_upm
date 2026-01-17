package es.upm.etsisi.poo.AppExceptions;

public class ExpiredException extends AppException {
    public ExpiredException(String productType, String id) {
        super(productType + " with ID " + id + " has expired");
    }

    public ExpiredException(String productType, String id, String msg) {
        super(productType + " with ID " + id + " has expired: " + msg);
    }
}
