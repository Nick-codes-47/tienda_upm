package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.App;

public class RemoveCashier extends RemoveUser {
    public RemoveCashier(App app) {
        super(app, app.cashiers);
    }

    @Override
    public int execute(String[] args) {
        String userId = args[0];
        app.tickets.removeTicketsFrom(userRegister.getUser(userId));
        userRegister.removeUser(userId);
        return 0;
    }

    @Override
    public String help() {
        return "cash remove <id>";
    }
}
