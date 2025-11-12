package es.upm.etsisi.poo.Requests.Handlers;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.Actions.TicketHandlerActions.*;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Requests.RequestHandler;

import java.util.HashMap;

public class TicketHandler extends RequestHandler {
    public TicketHandler(App app) {
        super();
        HashMap<String,Action> actions = super.getActions();
        actions.put("new", new AddNewTicket(app));
        actions.put("add", new AddProductToTicket(app));
        actions.put("remove", new RemoveProductFromTicket(app));
        actions.put("print",new PrintTicket(app));
        actions.put("list", new ListTickets(app));
    }
}
