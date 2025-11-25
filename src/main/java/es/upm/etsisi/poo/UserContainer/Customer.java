package es.upm.etsisi.poo.UserContainer;

public class Customer extends User {
    public Customer(String name, Email email, String dni, String cashierId) {
        super(name, email);
        this.cashierId = cashierId;
        this.id = dni;
    }
    
    public String getCashierId() {
        return cashierId;
    }
    
    public String getDni() {
        return id;
    }
    
    private final String cashierId;
}
