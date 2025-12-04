package es.upm.etsisi.poo.UserContainer;

public class Customer extends User {

    public Customer(String dni, String name, Email email, String cashierId) {
        super(dni, name, email); // TODO: verify dnis
        this.cashierId = cashierId;
    }
    
    public String getCashierId() {
        return cashierId;
    }
    
    public String getDni() { return id; }

    public static String getType() {
        return TYPE;
    }

    @Override
    protected String addVarToPrint() {
        return ", cash='" + cashierId + "'";
    };

    private final String cashierId;

    private static final String TYPE = "Client";
}
