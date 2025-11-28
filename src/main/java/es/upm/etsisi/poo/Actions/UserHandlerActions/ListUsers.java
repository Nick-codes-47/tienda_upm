package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.UserContainer.User;
import es.upm.etsisi.poo.UserContainer.UserRegister;

public class ListUsers extends UserAction {
    public ListUsers(App app, UserRegister userRegister) {
        super(app, userRegister);
    }

    @Override
    public int execute(String[] args) {
        User[] users = userRegister.getUsers();
        for (User user : users)
            System.out.println(user);
        return 0;
    }

    @Override
    public String help() {
        return "client list";
    }
}
