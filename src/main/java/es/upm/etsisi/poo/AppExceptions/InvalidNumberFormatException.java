package es.upm.etsisi.poo.AppExceptions;

public class InvalidNumberFormatException extends AppException {
    public InvalidNumberFormatException(String msg) {
        super("Error in the number format: " + msg);
    }
}
