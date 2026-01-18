package es.upm.etsisi.poo.AppExceptions.EntityExceptions;

import es.upm.etsisi.poo.AppExceptions.AppException;

public class NullAppEntityException extends AppException {
    public NullAppEntityException(String type) {
        super(type + " is null");
    }
}
