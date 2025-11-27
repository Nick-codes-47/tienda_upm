package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.UserContainer.Email;
import es.upm.etsisi.poo.UserContainer.User;
import es.upm.etsisi.poo.UserContainer.UserRegister;

public class AddUser extends UserAction {

    public AddUser(App app, UserRegister userRegister) {
        super(app, userRegister);
    }

    public int execute(String[] args) {
        User user = createUser(args);
        
        return userRegister.addUser(user);
    }

    @Override
    public String help() {
        return "cash add [<id>] \"<nombre>\"<email>";
    }

    protected User createUser(String[] args) {
        return new User(args[0], new Email(args[1]));
    }
}
