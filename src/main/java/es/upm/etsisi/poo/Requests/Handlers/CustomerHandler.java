package es.upm.etsisi.poo.Requests.Handlers;

import es.upm.etsisi.poo.Actions.UserHandlerActions.AddCustomer;
import es.upm.etsisi.poo.Actions.UserHandlerActions.AddUser;
import es.upm.etsisi.poo.Actions.UserHandlerActions.ListUsers;
import es.upm.etsisi.poo.Actions.UserHandlerActions.RemoveUser;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Requests.RequestHandler;

public class CustomerHandler extends RequestHandler {
    public static String handler_id = "client";

    public CustomerHandler() {
        super(handler_id);
        actions.put(AddUser.ID, new AddCustomer());
        actions.put(RemoveUser.ID, new RemoveUser(App.getInstance().customers));
        actions.put(ListUsers.ID, new ListUsers(App.getInstance().customers));
    }
}
