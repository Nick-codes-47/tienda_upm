package es.upm.etsisi.poo.Commands.User;

import es.upm.etsisi.poo.AppExceptions.EntityExceptions.EntityAlreadyExistsException;
import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;
import es.upm.etsisi.poo.AppExceptions.ArgumentExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Models.User.Core.CashID;
import es.upm.etsisi.poo.Models.User.Core.Cashier;
import es.upm.etsisi.poo.Models.User.Core.Email;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;

public class AddCashier extends AddUser<Cashier> {
    public static final String ID = "add";

    public AddCashier(UserRegister<Cashier> cashiers) {
        super(cashiers);
    }

    @Override
    protected Cashier createUser(String[] args)
            throws WrongNumberOfArgsException, InvalidEmailException, EntityAlreadyExistsException, InvalidAppIDException {
        CashID id;
        String nombre;
        Email email;
        if (args.length == 2) { // TODO: repeated code
            id = new CashID(getNewId());
            nombre = args[0];
            email = parseEmail(args[1]);
        }
        else if (args.length == 3) {
            id = new CashID(args[0]);
            nombre = args[1];
            email = parseEmail(args[2]);
            if (userRegister.getUser(id.toString()) != null)
                throw new EntityAlreadyExistsException("Cashier", id.toString());
        } else {
            throw new WrongNumberOfArgsException(this);
        }

        return new Cashier(id, nombre, email);
    }

    public String getNewId() {
        int nexIDVal = 0;
        while (true) {
            String id = String.format("UW%07d", nexIDVal++);
            if (userRegister.getUser(id) == null)
                return id;
        }
    }

    @Override
    public String help() {
        return ID + " [<id>] \"<nombre>\"<email>";
    }
}