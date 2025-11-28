package es.upm.etsisi.poo.Requests.Handlers;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.Actions.TicketHandlerActions.*;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Requests.RequestHandler;

import java.util.HashMap;

public class TicketHandler extends RequestHandler {
    public static String handler_id = "ticket";

    public TicketHandler() {
        super(handler_id);
        actions.put("new", new AddNewTicket());
        actions.put("add", new AddProductToTicket());
        actions.put("remove", new RemoveProductFromTicket());
        actions.put("print",new PrintTicket());
        actions.put("list", new ListTickets());
    }
}
