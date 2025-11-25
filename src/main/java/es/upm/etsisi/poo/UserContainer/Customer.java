package es.upm.etsisi.poo.UserContainer;

public class Customer extends User {
    private final String cashierId;
    private final String dni;

    public Customer(String name, Email email, String dni, String cashierId) {
        super(name, email);
        this.dni = dni;
        this.cashierId = cashierId;
    }

    public String getCashierId() {
        return cashierId;
    }

    public String getDni() {
        return dni;
    }
}
