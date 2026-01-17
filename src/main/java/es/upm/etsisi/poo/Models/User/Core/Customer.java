package es.upm.etsisi.poo.Models.User.Core;

import es.upm.etsisi.poo.AppExceptions.EntityAlreadyExistsException;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.User.UserEnums.ClientType;

import java.util.ArrayList;

public class Customer extends User {

    private static final long serialVersionUID = 1L;

    public Customer(UserNIF id, String name, Email email, String cashierId) {
        super(id.toString(), name, email); // TODO: verify dnis

        this.cashierId = cashierId;

        if (id.isCompany()) {
            this.type = ClientType.COMPANY;
        } else {
            this.type = ClientType.INDIVIDUAL;
        }

    }

    private boolean isCompany(String possibleNif) {
        return Character.isLetter(possibleNif.charAt(0)); // NIF starts with letter while DNI with number
    }

    public String getCashierId() {
        return cashierId;
    }

    public String getDni() { return id; }

    public ClientType getType() {
        return type;
    }

    public void addTicket(TicketID ticketID) throws EntityAlreadyExistsException{
        if (tickets.contains(ticketID)) throw new EntityAlreadyExistsException("ticket", ticketID.toString());

        tickets.add(ticketID);
    }

    @Override // TODO explain
    protected String addVarToPrint() {
        return ", cash='" + cashierId + "'";
    }

    private final String cashierId;

    private final ClientType type;

    private final ArrayList<TicketID> tickets = new ArrayList<>();
}