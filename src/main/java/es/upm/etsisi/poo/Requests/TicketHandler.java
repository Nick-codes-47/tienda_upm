package es.upm.etsisi.poo.Requests;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.Actions.TicketHandlerActions.*;
import es.upm.etsisi.poo.App;

import java.util.HashMap;

public class TicketHandler extends RequestHandler {
    public TicketHandler(App app) {
        super();
        // We obtain the map of actions
        HashMap<String,Action> actions = super.getActions();
        // We introduce all the actions that we'll need
        actions.put("new", new AddTicket(app));
        actions.put("add", new AddProductToTicket(app));
        actions.put("remove", new RemoveProductFromTicket(app));
        actions.put("print",new PrintTicket(app));
        actions.put("list", new ListTickets(app));
    }
}
