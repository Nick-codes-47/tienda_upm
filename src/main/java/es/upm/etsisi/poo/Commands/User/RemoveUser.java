package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Models.User.Core.User;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;

public class RemoveUser<T extends User> extends UserCommand<T> {

    public static final String ID = "remove";

    public RemoveUser(UserRegister<T> userRegister) {
        super(userRegister);
    }

    @Override
    public void execute(String[] args) throws WrongNumberOfArgsException, AppEntityNotFoundException {
        if (args.length != 1){ throw new WrongNumberOfArgsException(this); }

        String userId = args[0];
        userRegister.removeUser(userId);
    }

    @Override
    public String help() {
        return ID + " <DNI>";
    }
}
