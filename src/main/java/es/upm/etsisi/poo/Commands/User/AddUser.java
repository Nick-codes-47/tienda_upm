package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.Models.User.Users.Email;
import es.upm.etsisi.poo.Models.User.Users.User;
import es.upm.etsisi.poo.Models.User.UserRegister;

public abstract class AddUser<T extends User> extends UserCommand<T> {

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
