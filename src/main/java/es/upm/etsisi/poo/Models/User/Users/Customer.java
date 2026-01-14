package es.upm.etsisi.poo.Models.User.Users;

import es.upm.etsisi.poo.Models.User.UserEnums.ClientType;

import java.util.ArrayList;

public class Customer extends User {

    public Customer(String identification, String name, Email email, String cashierId) {
        super(identification, name, email); // TODO: verify dnis

        this.cashierId = cashierId;

        if (isCompany(identification)) {
            this.type = ClientType.COMPANY;
        } else {
            this.type = ClientType.INDIVIDUAL;
        }

    }

    public String getCashierId() {
        return cashierId;
    }
    
    public String getDni() { return id; }

    public ClientType getType() {
        return type;
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

    private boolean isCompany(String possibleNif) {
        return Character.isLetter(possibleNif.charAt(0)); // NIF starts with letter while DNI with number
    }

    private final String cashierId;

    private final ClientType type;

    private final ArrayList<String> tickets = new ArrayList<>();
}
