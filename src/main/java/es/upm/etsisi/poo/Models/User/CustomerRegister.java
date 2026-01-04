package es.upm.etsisi.poo.Models.User;

import es.upm.etsisi.poo.Models.User.Users.Customer;

public class CustomerRegister extends UserRegister<Customer> {

    public CustomerRegister() { super(Customer.getType()); }

    @Override
    public boolean isValidId(String id) {
        return true;
    }
}
