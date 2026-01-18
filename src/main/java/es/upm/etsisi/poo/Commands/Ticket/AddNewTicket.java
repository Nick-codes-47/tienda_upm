package es.upm.etsisi.poo.Commands.Ticket;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Ticket.CommonTicket;
import es.upm.etsisi.poo.Models.Ticket.CompanyTicket;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;
import es.upm.etsisi.poo.Models.Ticket.ServiceTicket;
import es.upm.etsisi.poo.Models.User.Core.*;
import es.upm.etsisi.poo.Services.TicketService;

public class AddNewTicket implements Command {
    public static final String ID = "new";

    private final TicketService ticketService;
    private final UserRegister<Cashier> cashiers;
    private final UserRegister<Customer> customers;

    public AddNewTicket(TicketService ticketService, UserRegister<Cashier> cashiers, UserRegister<Customer> customers) {
        this.ticketService = ticketService;
        this.cashiers = cashiers;
        this.customers = customers;
    }

    @Override
    public int execute(String[] args) throws AppException {
        TicketID ticketId = null;
        CashID cashId;
        UserNIF customerId;
        TicketType type = TicketType.PRODUCT;

        if (args.length == 2) {
            cashId = new CashID(args[0]);
            customerId = new UserNIF(args[1]);
        } else if (args.length == 3 && args[0].charAt(0) != 'U') {
            ticketId = new TicketID(args[0]);
            cashId = new CashID(args[1]);
            customerId = new UserNIF(args[2]);
        } else if (args.length == 3 && args[2].charAt(0) == '-' && args[2].length() == 2) {
            cashId = new CashID(args[0]);
            customerId = new UserNIF(args[1]);
            type = TicketType.getType(args[2].charAt(1));
            if (type == null) throw new WrongCommandArgumentsException("", this);
        } else if (args.length == 4 && args[3].charAt(0) == '-' && args[3].length() == 2) {
            ticketId = new TicketID(args[0]);
            cashId = new CashID(args[1]);
            customerId = new UserNIF(args[2]);
            type = TicketType.getType(args[3].charAt(1));
            if (type == null) throw new WrongCommandArgumentsException("", this);
        } else {
            throw new WrongNumberOfArgsException(this);
        }

        Cashier cashier = cashiers.getUser(cashId.toString());
        if (cashier == null) throw new AppEntityNotFoundException("cashier", cashId.toString());

        Customer customer = customers.getUser(customerId.toString());
        if (customer == null) throw new AppEntityNotFoundException("customer", customerId.toString());

        if (!customer.isCompany() && (type == TicketType.SERVICE || type == TicketType.COMBINED))
            throw new AppException("Only Companies can open Service or Combined tickets.");

        if (ticketId == null)
            ticketId = ticketService.getNewTicketID();
        Ticket<?> ticket = ticketFactory(type, ticketId);

        cashier.addTicket(ticket);
        customer.addTicket(ticketId);

        return 0;
    }

    private Ticket<?> ticketFactory(TicketType type, TicketID id) {
        return switch (type) {
            case PRODUCT -> new CommonTicket(id);
            case SERVICE -> new ServiceTicket(id);
            case COMBINED -> new CompanyTicket(id);
        };
    }

    private enum TicketType {
        PRODUCT, SERVICE, COMBINED;

        static TicketType getType(char c) {
            return switch (c) {
                case 'p': yield PRODUCT;
                case 's': yield SERVICE;
                case 'c': yield COMBINED;
                default: yield null;
            };
        }
    }

    @Override
    public String help() {
        return ID +" [<ticketId>] <cashId> <customerId> -[c|p|s]";
    }
}