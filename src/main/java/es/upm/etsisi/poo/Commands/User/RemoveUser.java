package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.Models.User.Users.User;
import es.upm.etsisi.poo.Models.User.UserRegister;

public class RemoveUser<T extends User> extends UserCommand<T> {

    public static final String ID = "remove";

    public RemoveUser(UserRegister<T> userRegister) {
        super(userRegister);
    }

    @Override
    public int execute(String[] args) {
        String userId = args[0];
        return userRegister.removeUser(userId);
    }

    @Override
    public String help() {
        return ID + " <DNI>";
    }
}
