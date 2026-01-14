package es.upm.etsisi.poo.Handlers;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Commands.Ticket.*;

public class TicketHandler extends RequestHandler {
    public static String handler_id = "ticket";

    public TicketHandler(App app) {
        super(handler_id);
        commands.put(AddNewTicket.ID, () -> new AddNewTicket(app.cashiers, app.customers));
        commands.put(AddProductToTicket.ID,() -> new AddProductToTicket(app.catalog, app.cashiers));
        commands.put(RemoveProductFromTicket.ID, () -> new RemoveProductFromTicket(app.cashiers));
        commands.put(PrintTicket.ID, () -> new PrintTicket(app.cashiers));
        commands.put(ListTickets.ID, () -> new ListTickets(app.ticketService));
    }
}
