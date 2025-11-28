package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.UserContainer.UserRegister;

public abstract class UserAction implements Action {
    public UserAction(UserRegister userRegister) {
        this.userRegister = userRegister;
    }

    protected final UserRegister userRegister;
}
