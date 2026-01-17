package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.User.Core.User;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;

public abstract class UserCommand<T extends User> implements Command {
    public UserCommand(UserRegister<T> userRegister) {
        this.userRegister = userRegister;
    }

    protected final UserRegister<T> userRegister;
}
