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
    public String help() {
        return ID + " <id>";
    }
}
