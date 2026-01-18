package es.upm.etsisi.poo.AppExceptions.ContainerExceptions;

import es.upm.etsisi.poo.AppExceptions.AppException;

public class FullContainerException extends AppException {
    public FullContainerException() {
        super("Maximum number of items reached.");
    }
}
