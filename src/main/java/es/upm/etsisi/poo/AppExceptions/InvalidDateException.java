package es.upm.etsisi.poo.AppExceptions;

import java.time.LocalDate;

public class InvalidDateException extends AppException {
    public InvalidDateException() {
        super("Date can't be before: " + LocalDate.now());
    }
}
