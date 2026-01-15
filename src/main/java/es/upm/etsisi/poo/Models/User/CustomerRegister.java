package es.upm.etsisi.poo.Models.User;

import es.upm.etsisi.poo.Models.User.UserEnums.UserType;
import es.upm.etsisi.poo.Models.User.Users.Customer;

public class CustomerRegister extends UserRegister<Customer> {

    private static final long serialVersionUID = 1L;

    public CustomerRegister() { super(UserType.CLIENT); }

    @Override
    public boolean isValidId(String id) {
        return true;
    }
}