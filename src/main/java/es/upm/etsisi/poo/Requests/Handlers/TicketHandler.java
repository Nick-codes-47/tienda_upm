package es.upm.etsisi.poo.Requests.Handlers;

import es.upm.etsisi.poo.Actions.TicketHandlerActions.*;
import es.upm.etsisi.poo.Requests.RequestHandler;

public class TicketHandler extends RequestHandler {
    public static String handler_id = "ticket";

    public TicketHandler() {
        super(handler_id);
        actions.put(AddNewTicket.ID, new AddNewTicket());
        actions.put(AddProductToTicket.ID, new AddProductToTicket());
        actions.put(RemoveProductFromTicket.ID, new RemoveProductFromTicket());
        actions.put(PrintTicket.ID,new PrintTicket());
        actions.put(ListTickets.ID, new ListTickets());
    }
}
