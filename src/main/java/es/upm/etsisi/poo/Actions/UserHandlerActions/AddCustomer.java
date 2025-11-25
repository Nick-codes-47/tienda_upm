package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.UserContainer.User;
import es.upm.etsisi.poo.UserContainer.Email;
import es.upm.etsisi.poo.UserContainer.Customer;

public class AddCustomer extends AddUser {
    public AddCustomer(App app) {
        super(app, app.customers);
    }

    @Override
    protected User createUser(String[] args) { // TODO: better use a factory ?
        if (app.cashiers.getUser(args[3]) != null)
            return null;

        return new Customer(args[0], new Email(args[2]), args[1], args[3]);
    }
}