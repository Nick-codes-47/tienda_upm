package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.UserContainer.User;
import es.upm.etsisi.poo.UserContainer.Email;
import es.upm.etsisi.poo.UserContainer.Customer;

import java.util.Objects;

public class AddCustomer extends AddUser {
    public AddCustomer(App app) {
        super(app, app.customers);
    }

    @Override
    protected User createUser(String[] args) { // TODO: better use a factory ?
        String id, name, dni, cashId;
        Email email;
        if (args.length == 4) { // TODO: repeated code
            id = args[1];
            dni = id;
            name = args[0];
            email = new Email(args[2]);
            cashId = args[3];
        }
        else if (args.length == 5) {
            id = args[0];
            dni = args[2];
            name = args[1];
            email = new Email(args[3]);
            cashId = args[4];
            if (!Objects.equals(id, dni)) return null; // TODO: print error
        } else return null; // TODO print error

        if (app.cashiers.getUser(cashId) != null) // TODO: print error
            return null;

        return new Customer(dni, name, email, cashId);
    }

    @Override
    public String help() {
        return "client add \"<nombre>\" <DNI> <email> <cashId>";
    }
}