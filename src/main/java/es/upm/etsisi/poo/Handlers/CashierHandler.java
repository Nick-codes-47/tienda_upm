package es.upm.etsisi.poo.Handlers;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Commands.Ticket.ListTicketsFromCashier;
import es.upm.etsisi.poo.Commands.User.AddCashier;
import es.upm.etsisi.poo.Commands.User.ListUsers;
import es.upm.etsisi.poo.Commands.User.RemoveCashier;

public class CashierHandler extends RequestHandler {
    public static String handler_id = "cash";

    public CashierHandler(App app) {
        super(handler_id);
        commands.put(AddCashier.ID, () -> new AddCashier(App.cashiers));
        commands.put(RemoveCashier.ID, () -> new RemoveCashier(App.cashiers));
        commands.put(ListUsers.ID, () -> new ListUsers<>(App.cashiers));
        commands.put(ListTicketsFromCashier.ID, () -> new ListTicketsFromCashier(App.cashiers, app.ticketService));
    }
}
