package es.upm.etsisi.poo.Requests.Handlers;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.Actions.UserHandlerActions.AddCustomer;
import es.upm.etsisi.poo.Actions.UserHandlerActions.ListUsers;
import es.upm.etsisi.poo.Actions.UserHandlerActions.RemoveUser;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Requests.RequestHandler;

import java.util.HashMap;

public class CustomerHandler extends RequestHandler {
    public static String handler_id = "client";

    public CustomerHandler() {
        super(handler_id);
        actions.put("add", new AddCustomer());
        actions.put("remove", new RemoveUser(App.getInstance().customers));
        actions.put("list", new ListUsers(App.getInstance().customers));
    }
}
