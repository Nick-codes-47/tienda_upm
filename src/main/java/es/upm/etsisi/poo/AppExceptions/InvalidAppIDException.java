package es.upm.etsisi.poo.AppExceptions;

public class InvalidAppIDException extends AppException {
    public InvalidAppIDException() {
        super("ID is invalid");
    }

    public InvalidAppIDException(String message) {
        super("ID is invalid: " + message);
    }
}
