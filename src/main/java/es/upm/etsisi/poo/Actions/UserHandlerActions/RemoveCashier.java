package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.App;

public class RemoveCashier extends RemoveUser {
    public static final String ID = "remove";

    public RemoveCashier() {
        super(App.getInstance().cashiers);
    }

    @Override
    public int execute(String[] args) {

        if (args.length != 1) {
            System.err.println("Wrong number of input args");
            return 1;
        }

        String userId = args[0];
        if (userRegister.getUser(userId) == null) {
            System.err.printf("cashier {%s} does not exist\n", userId);
            return 2;
        }
        App.getInstance().tickets.removeTicketsFrom(userId);
        userRegister.removeUser(userId);
        return 0;
    }

    @Override
    public String help() {
        return ID + " <id>";
    }
}
