package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;
import es.upm.etsisi.poo.AppExceptions.InvalidEmailException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Models.User.Users.Email;
import es.upm.etsisi.poo.Models.User.Users.User;
import es.upm.etsisi.poo.Models.User.UserRegister;

public abstract class AddUser<T extends User> extends UserCommand<T> {

    public AddUser(UserRegister<T> userRegister) {
        super(userRegister);
    }

    public int execute(String[] args) throws AppException {
        T user = createUser(args);
        
        userRegister.addUser(user);

        AppLogger.info(user.toString());

        return 0;
    }

    protected abstract T createUser(String[] args) throws AppException;

    protected Email parseEmail(String rawEmail) throws InvalidEmailException
    {
        String[] parts = rawEmail.split("@", 2);

        if (parts.length != 2) throw new InvalidEmailException();

        return new Email(parts[0], parts[1]);
    }
}
