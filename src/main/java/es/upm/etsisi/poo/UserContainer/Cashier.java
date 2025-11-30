package es.upm.etsisi.poo.UserContainer;

public class Cashier extends User {

    public Cashier(String id, String name, Email email) {
        super(id, name, email);
    }

    public static String getType() {
        return TYPE;
    }

    private static final String TYPE = "Cash";
}
