package es.upm.etsisi.poo.AppExceptions.ContainerExceptions;

import es.upm.etsisi.poo.AppExceptions.AppException;

public class EmptyContainerException extends AppException {
    public EmptyContainerException(String model) {
        super("There are no "+ model +" yet");
    }
}
