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
        // TODO ALL
        return 0;
    }

    @Override
    public String help() {
        // TODO ALL
        return "";
    }
}
