package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.App;

public class RemoveCashier extends RemoveUser {
    public RemoveCashier(App app) {
        super(app, app.cashiers);
    }

    @Override
    public int execute(String[] args) {
        // TODO ALL
        return 0;
    }

    @Override
    public String help() {
        // TODO ALL
        return "";
    }
}
