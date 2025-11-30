package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.UserContainer.UserRegister;

public class RemoveUser extends UserAction {

    public static final String ID = "remove";

    public RemoveUser(UserRegister userRegister) {
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
