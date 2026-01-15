package es.upm.etsisi.poo.AppExceptions;

public class EmptyDataException extends AppException {
    public EmptyDataException(String model) {
        super("There are no "+ model +" yet");
    }
}
