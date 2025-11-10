package es.upm.etsisi.poo.Requests;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.Actions.TicketHandlerActions.ListTickets;
import es.upm.etsisi.poo.Actions.UserHandlerActions.AddUser;
import es.upm.etsisi.poo.Actions.UserHandlerActions.ListUsers;
import es.upm.etsisi.poo.Actions.UserHandlerActions.RemoveCashier;
import es.upm.etsisi.poo.App;

import java.util.HashMap;

public class CashierHandler extends RequestHandler {
    public CashierHandler(App app) {
        super();
        HashMap<String,Action> actions = super.getActions();
        actions.put("add", new AddUser(app));
        actions.put("remove", new RemoveCashier(app));
        actions.put("list", new ListUsers(app));
        actions.put("tickets", new ListTickets(app));
    }
}
