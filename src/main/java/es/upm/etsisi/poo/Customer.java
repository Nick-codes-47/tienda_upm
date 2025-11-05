package es.upm.etsisi.poo;

public class Customer extends User {
    private String cashierId;

    public Customer(String name, Email email, String cashierId) {
        super(name, email);
        this.cashierId = cashierId;
    }

    public String getCashierId() {
        return cashierId;
    }
}
