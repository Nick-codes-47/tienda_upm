package es.upm.etsisi.poo.AppExceptions;

import es.upm.etsisi.poo.App;

public class FullContainerException extends AppException {
    public FullContainerException() {
        super("Maximum number of items reached.");
    }
}
