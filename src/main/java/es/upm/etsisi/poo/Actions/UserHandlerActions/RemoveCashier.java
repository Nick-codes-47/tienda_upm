package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.App;

public class RemoveCashier extends RemoveUser {
    public static final String ID = "remove";

    public RemoveCashier() {
        super(App.getInstance().cashiers);
    }

    @Override
    public int execute(String[] args) {
        String userId = args[0];
        App.getInstance().tickets.removeTicketsFrom(userId);
        userRegister.removeUser(userId);
        return 0;
    }

    @Override
    public String help() {
        return ID + " <id>";
    }
}
