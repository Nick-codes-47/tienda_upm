package es.upm.etsisi.poo.Models.User.Core;

import es.upm.etsisi.poo.AppExceptions.EntityExceptions.EntityAlreadyExistsException;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;

import java.util.ArrayList;

public class Customer extends User {

    private static final long serialVersionUID = 1L;

    public Customer(UserNIF id, String name, Email email, String cashierId) {
        super(id.toString(), name, email); // TODO: verify dnis

        this.cashierId = cashierId;
    }

    public boolean isCompany() {
        return Character.isLetter(id.charAt(0)); // NIF starts with letter while DNI with number
    }

    public String getCashierId() {
        return cashierId;
    }

    public String getDni() { return id; }

    public void addTicket(TicketID ticketID) throws EntityAlreadyExistsException{
        if (tickets.contains(ticketID)) throw new EntityAlreadyExistsException("ticket", ticketID.toString());

        tickets.add(ticketID);
    }

    private final String cashierId;

    private final ArrayList<TicketID> tickets = new ArrayList<>();

    @Override
    public String toString() {
        return String.format(
                "%s{identifier='%s', name='%s', email:'%s'%s}",
                isCompany()? "Company":"Particular",
                id, name, email, ", cash='" + cashierId + "'");
    }
}