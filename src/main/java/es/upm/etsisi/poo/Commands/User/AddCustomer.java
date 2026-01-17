package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.InvalidEmailException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Models.User.*;
import es.upm.etsisi.poo.Models.User.Users.Customer;
import es.upm.etsisi.poo.Models.User.Users.Email;

public class AddCustomer extends AddUser<Customer> {
    public static final String ID = "add";

    public AddCustomer(CustomerRegister customers, CashierRegister cashiers) {
        super(customers);
        this.cashiers = cashiers;
    }

    @Override
    protected Customer createUser(String[] args)
            throws WrongNumberOfArgsException, InvalidEmailException, AppEntityNotFoundException {
        // TODO: better use a factory ?
        if (args.length != 4) throw new WrongNumberOfArgsException();

        String name = args[0];
        String identification = args[1];
        Email email = parseEmail(args[2]);
        String cashId = args[3];

        if (cashiers.getUser(cashId) == null) throw new AppEntityNotFoundException("cashier", cashId);

        return new Customer(identification, name, email, cashId);
    }

    @Override
    public String help() {
        return ID + " \"<nombre>\" (<DNI>|<NIF>) <email> <cashId>";
    }

    private final CashierRegister cashiers;
}