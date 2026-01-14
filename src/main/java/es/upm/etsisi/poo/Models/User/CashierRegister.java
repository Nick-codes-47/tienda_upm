package es.upm.etsisi.poo.Models.User;

import es.upm.etsisi.poo.Models.User.UserEnums.UserType;
import es.upm.etsisi.poo.Models.User.Users.Cashier;

public class CashierRegister extends UserRegister<Cashier> {

    public CashierRegister() { super(UserType.CASHIER); }

    @Override
    public String getNewId() {
        String id;
        while (true) {
            id = String.format("UW%07d", nextIdVal++);
            if (getUser(id) == null)
                return id;
        }
    }

    @Override
    public boolean isValidId(String id) {
        return id.length() == 9 && id.charAt(0) == 'U' && id.charAt(1) == 'W';
    }

    private static int nextIdVal = 0;
}
