package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.Containers.User.Cashier;
import es.upm.etsisi.poo.Containers.User.Email;
import es.upm.etsisi.poo.Containers.User.User;
import es.upm.etsisi.poo.Containers.User.UserRegister;

public abstract class AddUser<T extends User> extends UserCommand<T> {
    public static final String ID = "add";

    public AddUser(UserRegister<T> userRegister) {
        super(userRegister);
    }

    public int execute(String[] args) {
        T user = createUser(args);
        
        int ret = userRegister.addUser(user);
        if (ret == 0)
            System.out.println(user);

        return ret;
    }

    protected abstract T createUser(String[] args);

    protected Email parseEmail(String rawEmail)
    {
        String[] parts = rawEmail.split("@", 2);

        if (parts.length != 2)
            return null;

        return new Email(parts[0], parts[1]);
    }
}
