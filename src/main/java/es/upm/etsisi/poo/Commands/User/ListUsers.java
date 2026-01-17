package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.EmptyDataException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Models.User.Core.User;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;

import java.util.List;

public class ListUsers<T extends User> extends UserCommand<T> {
    public static final String ID = "list";

    public ListUsers(UserRegister<T> userRegister) {
        super(userRegister);
    }

    @Override
    public int execute(String[] args) throws AppException {
        if (args.length != 0){ throw new WrongNumberOfArgsException(this); }

        List<T> users = userRegister.getUsers();

        if (users.isEmpty()) throw new EmptyDataException("users");

        StringBuilder sb = null;
        for (User user : users) {
            if (sb == null)
                sb = new StringBuilder(user.getClass() + ":");
            sb.append("  ").append(user);
        }

        AppLogger.info(sb.toString());
        return 0;
    }

    @Override
    public String help() {
        return ID;
    }
}
