package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.UserContainer.Cashier;
import es.upm.etsisi.poo.UserContainer.Email;
import es.upm.etsisi.poo.UserContainer.User;
import es.upm.etsisi.poo.UserContainer.UserRegister;

public class AddUser extends UserAction {
    public static final String ID = "add";

    public AddUser(UserRegister userRegister) {
        super(userRegister);
    }

    public int execute(String[] args) {
        User user = createUser(args);
        
        int ret = userRegister.addUser(user);
        if (ret != 0)
            System.err.printf("Error addUser %d\n", ret);
        else
            System.out.println(user);

        return ret;
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
            if (userRegister.getUser(id) != null) {
                System.err.printf("cashier {%s} already exists\n", id);
                return null;
            }
        } else {
            System.err.println("Wrong number of input args");
            return null;
        }
        return new Cashier(id, nombre, email);
    }

    @Override
    public String help() {
        return ID + " [<id>] \"<nombre>\"<email>";
    }
}
