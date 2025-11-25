package es.upm.etsisi.poo.Requests.Handlers;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.Actions.TicketHandlerActions.ListTickets;
import es.upm.etsisi.poo.Actions.UserHandlerActions.AddUser;
import es.upm.etsisi.poo.Actions.UserHandlerActions.ListUsers;
import es.upm.etsisi.poo.Actions.UserHandlerActions.RemoveCashier;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Requests.RequestHandler;

import java.util.HashMap;

public class CashierHandler extends RequestHandler {
    public CashierHandler(App app) {
        super();
        // We obtain the map of actions
        HashMap<String,Action> actions = super.getActions();
        // We introduce all the actions that we'll need
        actions.put("add", new AddUser(app, app.cashiers));
        actions.put("remove", new RemoveCashier(app));
        actions.put("list", new ListUsers(app, app.cashiers));
        actions.put("tickets", new ListTickets(app));
    }
}
