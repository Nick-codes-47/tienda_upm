package es.upm.etsisi.poo.AppExceptions.DateExceptions;

import es.upm.etsisi.poo.AppExceptions.AppException;

public class InvalidDateFormatException extends AppException {
    public InvalidDateFormatException() {
        super("Invalid date format. Must be: yyyy-MM-dd");
    }
}
