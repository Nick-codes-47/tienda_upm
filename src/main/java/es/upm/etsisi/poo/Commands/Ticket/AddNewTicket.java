package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Ticket.CommonTicket;
import es.upm.etsisi.poo.Models.Ticket.CompanyTicket;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.User.UserEnums.ClientType;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.CashierRegister;
import es.upm.etsisi.poo.Models.User.Users.Customer;
import es.upm.etsisi.poo.Models.User.CustomerRegister;
import es.upm.etsisi.poo.Services.TicketService;

public class AddNewTicket implements Command {
    public static final String ID = "new";

    private final TicketService ticketService;
    private final CashierRegister cashiers;
    private final CustomerRegister customers;

    public AddNewTicket(TicketService ticketService, CashierRegister cashiers, CustomerRegister customers) {
        this.ticketService = ticketService;
        this.cashiers = cashiers;
        this.customers = customers;
    }

    @Override
    public int execute(String[] args) throws AppException {
        String ticketId = null;
        String cashId;
        String customerId;

        if (args.length == 2) {
            cashId = args[0];
            customerId = args[1];
        } else if (args.length == 3) {
            ticketId = args[0];
            cashId = args[1];
            customerId = args[2];
        } else {
            throw new WrongNumberOfArgsException();
        }

        if(ticketId != null){ // TODO generate new
            if (!ticketId.matches("\\d+")) throw new InvalidAppIDException("Ticket ID must be numeric.");

            if (ticketId.length() != 5) throw new InvalidAppIDException("Ticket ID has to be 5 digits.");
        }

        Cashier cashier = cashiers.getUser(cashId);
        if (cashier == null) throw new AppEntityNotFoundException("cashier", cashId);

        Customer customer = customers.getUser(customerId);
        if (customer == null) throw new AppEntityNotFoundException("customer", customerId);

        // TODO check the ticketID is not in another cashier

        Ticket<?> ticket;
        TicketID ID;

        if (ticketId != null)
            ID = new TicketID(ticketId);
        else
            ID = ticketService.getNewTicketID();

        if (customer.getType() == ClientType.COMPANY)
            ticket = new CompanyTicket(ID);
        else
            ticket = new CommonTicket(ID);

        cashier.addTicket(ticket);

        customer.addTicket(ID);

        return 0;
    }

    @Override
    public String help() {
        return ID +" [<ticketId>] <cashId> <customerId>";
    }
}