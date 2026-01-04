package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.CashierRegister;

public class RemoveCashier extends RemoveUser<Cashier> {
    public static final String ID = "remove";

    public RemoveCashier(CashierRegister cashiers) {
        super(cashiers);
    }

    @Override
    public int execute(String[] args) {

        if (args.length != 1) {
            System.err.println("Wrong number of input args");
            return 1;
        }

        String userId = args[0];
        if (userRegister.getUser(userId) == null) {
            System.err.printf("cashier {%s} does not exist\n", userId);
            return 2;
        }
        userRegister.removeUser(userId);
        return 0;
    }

    @Override
    public String help() {
        return ID + " <id>";
    }
}
