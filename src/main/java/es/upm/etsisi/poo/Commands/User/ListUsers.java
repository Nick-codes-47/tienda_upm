package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.EmptyDataException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Models.User.Users.User;
import es.upm.etsisi.poo.Models.User.UserRegister;

import java.util.List;

public class ListUsers<T extends User> extends UserCommand<T> {
    public static final String ID = "list";

    public ListUsers(UserRegister<T> userRegister) {
        super(userRegister);
    }

    @Override
    public int execute(String[] args) throws AppException {
        if (args.length != 0){ throw new WrongNumberOfArgsException(); }

        List<T> users = userRegister.getUsers();

        if (users.isEmpty()) throw new EmptyDataException("users");

        System.out.println(userRegister.USER_TYPE + ":");
        for (User user : users)
            System.out.println("  " + user);
        return 0;
    }

    @Override
    public String help() {
        return ID;
    }
}
