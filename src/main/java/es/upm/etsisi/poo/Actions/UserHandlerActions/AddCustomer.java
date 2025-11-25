package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.UserContainer.User;
import es.upm.etsisi.poo.UserContainer.Email;
import es.upm.etsisi.poo.UserContainer.Customer;

public class AddCustomer extends AddUser {
    public AddCustomer(App app) {
        super(app, app.customers);
    }

    User createUser(String[] args) {
        // if (!app.cashiers.containsKey(args[3]))
        //     return null; // TODO enable

        return new Customer(args[0], new Email(args[2]), args[1], args[3]);
    }

    String generateUsersId(Customer customer)
    {
        return customer.getDni();
    }
}