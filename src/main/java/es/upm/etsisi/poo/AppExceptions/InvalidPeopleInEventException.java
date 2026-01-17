package es.upm.etsisi.poo.AppExceptions;

public class InvalidPeopleInEventException extends AppException {
    public InvalidPeopleInEventException() {
        super("Events must have between 1 and 100 people");
    }
}
