package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.Models.User.Users.User;
import es.upm.etsisi.poo.Models.User.UserRegister;

import java.util.List;

public class ListUsers<T extends User> extends UserCommand<T> {
    public static final String ID = "list";

    public ListUsers(UserRegister<T> userRegister) {
        super(userRegister);
    }

    @Override
    public int execute(String[] args) {
        List<T> users = userRegister.getUsers();
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
