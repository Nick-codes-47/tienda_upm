package es.upm.etsisi.poo.AppExceptions;

public class AppEntityNotFoundException extends AppException {
    public AppEntityNotFoundException(String entityType, String entityId) {
        super("No such"+ entityType +" with id " + entityId);
    }
}
