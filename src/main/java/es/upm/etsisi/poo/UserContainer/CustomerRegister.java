package es.upm.etsisi.poo.UserContainer;

public class CustomerRegister extends UserRegister<Customer> {
    @Override
    public boolean isValidId(String id) {
        return true;
    }
}
