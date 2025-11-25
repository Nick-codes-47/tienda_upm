package es.upm.etsisi.poo.Requests.Handlers;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.Actions.UserHandlerActions.AddCustomer;
import es.upm.etsisi.poo.Actions.UserHandlerActions.AddUser;
import es.upm.etsisi.poo.Actions.UserHandlerActions.ListUsers;
import es.upm.etsisi.poo.Actions.UserHandlerActions.RemoveUser;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Requests.RequestHandler;

import java.util.HashMap;

public class CustomerHandler extends RequestHandler {
    public CustomerHandler(App app) {
        super();
        // We obtain the map of actions
        HashMap<String, Action> actions = super.getActions();
        // We introduce all the actions that we'll need
        actions.put("add", new AddCustomer(app));
        actions.put("remove", new RemoveUser(app, app.customers));
        actions.put("list", new ListUsers(app, app.customers));
    }
}
