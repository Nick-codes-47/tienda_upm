package es.upm.etsisi.poo.Containers.User;

public class CashierRegister extends UserRegister<Cashier> {

    public CashierRegister() { super(Cashier.getType()); }

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
