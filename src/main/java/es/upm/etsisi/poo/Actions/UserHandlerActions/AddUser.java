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
        String id, nombre;
        Email email;
        if (args.length == 2) { // TODO: repeated code
            id = userRegister.getNewId();
            nombre = args[0];
            email = new Email(args[1]);
        }
        else if (args.length == 3) {
            id = args[0];
            nombre = args[1];
            email = new Email(args[2]);
            if (userRegister.getUser(id) != null) //TODO: print errors
                return null;
        } else return null;
        return new User(id, nombre, email);
    }
}
