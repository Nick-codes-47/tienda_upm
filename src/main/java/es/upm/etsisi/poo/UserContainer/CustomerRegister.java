package es.upm.etsisi.poo.UserContainer;

public class CustomerRegister extends UserRegister<Customer> {

    public CustomerRegister() { super(Customer.getType()); }

    @Override
    public boolean isValidId(String id) {
        return true;
    }
}
