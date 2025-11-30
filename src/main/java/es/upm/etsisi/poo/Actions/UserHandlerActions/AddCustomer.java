package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.UserContainer.User;
import es.upm.etsisi.poo.UserContainer.Email;
import es.upm.etsisi.poo.UserContainer.Customer;

import java.util.Objects;

public class AddCustomer extends AddUser {
    public static final String ID = "add";

    public AddCustomer() {
        super(App.getInstance().customers);
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
            if (!Objects.equals(id, dni)) {
                System.err.printf("client id {%s} and dni {%s} must be the same\n", id, dni);
                return null;
            }
        } else {
            System.err.println("Wrong number of input args");
            return null;
        }

        if (App.getInstance().cashiers.getUser(cashId) == null) {
            System.err.printf("cashier {%s} does not exist\n", cashId);
            return null;
        }

        return new Customer(dni, name, email, cashId);
    }

    @Override
    public String help() {
        return ID +" \"<nombre>\" <DNI> <email> <cashId>";
    }
}