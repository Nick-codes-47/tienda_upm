package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.UserContainer.UserRegister;

public abstract class UserAction extends Action {
    public UserAction(App app, UserRegister userRegister) {
        super(app);
        this.userRegister = userRegister;
    }

    protected final UserRegister userRegister;
}
