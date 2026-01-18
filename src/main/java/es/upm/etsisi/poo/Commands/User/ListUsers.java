package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.ContainerExceptions.EmptyContainerException;
import es.upm.etsisi.poo.AppExceptions.ArgumentExceptions.WrongNumberOfArgsException;
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
    public void execute(String[] args) throws AppException {
        if (args.length != 0){ throw new WrongNumberOfArgsException(this); }

        List<T> users = userRegister.getUsers();

        if (users.isEmpty()) throw new EmptyContainerException("users");

        StringBuilder sb = null;
        for (User user : users) {
            if (sb == null)
                sb = new StringBuilder(user.getClass().getSimpleName() + ":");
            sb.append("\n").append("  ").append(user);
        }

        AppLogger.info(sb.toString());
    }

    @Override
    public String help() {
        return ID;
    }
}
