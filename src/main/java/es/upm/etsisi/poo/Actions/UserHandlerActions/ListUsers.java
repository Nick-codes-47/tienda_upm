package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.UserContainer.User;
import es.upm.etsisi.poo.UserContainer.UserRegister;

public class ListUsers extends UserAction {
    public static final String ID = "list";

    public ListUsers(UserRegister userRegister) {
        super(userRegister);
    }

    @Override
    public int execute(String[] args) {
        User[] users = userRegister.getUsers();
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
