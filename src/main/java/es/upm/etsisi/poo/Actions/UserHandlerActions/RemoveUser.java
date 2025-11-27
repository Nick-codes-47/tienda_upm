package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.UserContainer.UserRegister;

public class RemoveUser extends UserAction {
    public RemoveUser(App app, UserRegister userRegister) {
        super(app, userRegister);
    }

    @Override
    public int execute(String[] args) {
        String userId = args[0];
        return userRegister.removeUser(userId);
    }

    @Override
    public String help() {
        return "client remove <DNI>";
    }
}
