package es.upm.etsisi.poo.Handlers;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Commands.User.AddCustomer;
import es.upm.etsisi.poo.Commands.User.ListUsers;
import es.upm.etsisi.poo.Commands.User.RemoveUser;

public class CustomerHandler extends RequestHandler {
    public static String handler_id = "client";

    public CustomerHandler(App app) {
        super(handler_id);
        commands.put(AddCustomer.ID, () -> new AddCustomer(app.customers, app.cashiers));
        commands.put(RemoveUser.ID, () -> new RemoveUser<>(app.customers));
        commands.put(ListUsers.ID, () -> new ListUsers<>(app.customers));
    }
}
