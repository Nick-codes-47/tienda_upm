package es.upm.etsisi.poo.AppExceptions;

public class InvalidEmailException extends AppException {
    public InvalidEmailException() {
        super("Invalid email address");
    }
}
