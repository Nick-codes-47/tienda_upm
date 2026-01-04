package es.upm.etsisi.poo.Models.User.Users;

import java.util.ArrayList;

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

    public int addTicket(String ticketID) {
        if (tickets.contains(ticketID))
            return -1; // TODO exception

        tickets.add(ticketID);
        return 0;
    }

    @Override // TODO explain
    protected String addVarToPrint() {
        return ", cash='" + cashierId + "'";
    }

    private final String cashierId;

    private static final String TYPE = "Client";

    private final ArrayList<String> tickets = new ArrayList<>();
}
