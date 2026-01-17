package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Models.User.Core.Cashier;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;

public class RemoveCashier extends RemoveUser<Cashier> {
    public static final String ID = "remove";

    public RemoveCashier(UserRegister<Cashier> cashiers) {
        super(cashiers);
    }

    @Override
    public int execute(String[] args) throws WrongNumberOfArgsException, AppEntityNotFoundException {

        if (args.length != 1) throw new WrongNumberOfArgsException();

        String userId = args[0];

        userRegister.removeUser(userId);
        return 0;
    }

    @Override
    public String help() {
        return ID + " <id>";
    }
}
