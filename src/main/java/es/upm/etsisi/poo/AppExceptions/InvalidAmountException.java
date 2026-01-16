package es.upm.etsisi.poo.AppExceptions;

public class InvalidAmountException extends AppException {
    public InvalidAmountException() {
        super("Amount must be a positive integer");
    }
}
