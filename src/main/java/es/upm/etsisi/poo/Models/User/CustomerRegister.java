package es.upm.etsisi.poo.Models.User;

import es.upm.etsisi.poo.Models.User.UserEnums.UserType;
import es.upm.etsisi.poo.Models.User.Users.Customer;

public class CustomerRegister extends UserRegister<Customer> {

    public CustomerRegister() { super(UserType.CLIENT); }

    @Override
    public boolean isValidId(String id) {
        return true;
    }
}
