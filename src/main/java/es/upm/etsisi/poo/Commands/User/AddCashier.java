package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.Models.User.*;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.Users.Email;

public class AddCashier extends AddUser<Cashier> {
    public static final String ID = "add";

    public AddCashier(CashierRegister cashiers) {
        super(cashiers);
    }

    @Override
    protected Cashier createUser(String[] args) {
        String id, nombre;
        Email email;
        if (args.length == 2) { // TODO: repeated code
            id = userRegister.getNewId();
            nombre = args[0];
            email = parseEmail(args[1]);
        }
        else if (args.length == 3) {
            id = args[0];
            nombre = args[1];
            email = parseEmail(args[2]);
            if (userRegister.getUser(id) != null) {
                System.err.printf("cashier {%s} already exists\n", id);
                return null;
            }
        } else {
            System.err.println("Wrong number of input args");
            return null;
        }
        if (email == null) {
            System.err.println("Invalid email");
            return null;
        }

        return new Cashier(id, nombre, email);
    }

    @Override
    public String help() {
        return ID + " [<id>] \"<nombre>\"<email>";
    }
}