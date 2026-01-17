package es.upm.etsisi.poo.AppExceptions;

public class InvalidDateFormatException extends AppException {
    public InvalidDateFormatException() {
        super("Invalid date format. Must be: yyyy-MM-dd");
    }
}
