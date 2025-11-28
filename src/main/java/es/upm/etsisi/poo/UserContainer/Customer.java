package es.upm.etsisi.poo.UserContainer;

public class Customer extends User {
    public Customer(String dni, String name, Email email, String cashierId) {
        super(dni, name, email); // TODO: verufy dnis
        this.cashierId = cashierId;
    }
    
    public String getCashierId() {
        return cashierId;
    }
    
    public String getDni() { return id; }
    
    private final String cashierId;
}
