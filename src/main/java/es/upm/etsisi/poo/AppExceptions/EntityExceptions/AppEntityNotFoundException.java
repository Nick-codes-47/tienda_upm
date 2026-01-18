package es.upm.etsisi.poo.AppExceptions.EntityExceptions;

import es.upm.etsisi.poo.AppExceptions.AppException;

public class AppEntityNotFoundException extends AppException {
    public AppEntityNotFoundException(String entityType, String entityId) {
        super("No such "+ entityType +" with id " + entityId);
    }
}
