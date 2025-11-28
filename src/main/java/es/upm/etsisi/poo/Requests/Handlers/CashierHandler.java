package es.upm.etsisi.poo.Requests.Handlers;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.Actions.TicketHandlerActions.ListTickets;
import es.upm.etsisi.poo.Actions.TicketHandlerActions.ListTicketsFromCashier;
import es.upm.etsisi.poo.Actions.UserHandlerActions.AddUser;
import es.upm.etsisi.poo.Actions.UserHandlerActions.ListUsers;
import es.upm.etsisi.poo.Actions.UserHandlerActions.RemoveCashier;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Requests.RequestHandler;

import java.util.HashMap;

public class CashierHandler extends RequestHandler {
    public static String handler_id = "cash";

    public CashierHandler() {
        super(handler_id);
        actions.put("add", new AddUser(App.getInstance().cashiers));
        actions.put("remove", new RemoveCashier());
        actions.put("list", new ListUsers(App.getInstance().cashiers));
        actions.put("tickets", new ListTicketsFromCashier());
    }
}
