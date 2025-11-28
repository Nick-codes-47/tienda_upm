package es.upm.etsisi.poo.Requests;

import es.upm.etsisi.poo.Actions.Action;

import java.util.HashMap;

public abstract class RequestHandler {
    public final String HANDLER_ID;

    public RequestHandler(String handlerId) {
        HANDLER_ID = handlerId;
        actions = new HashMap<>();
    }

    public HashMap<String, Action> getActions() {
        return actions;
    }

    public Action getAction(String actionId) {
        // We look if we have that action in the map
        Action actionToReturn = this.actions.get(actionId);
        if (actionToReturn == null) {
            System.err.println("ERROR: No such action: " + actionId);
        }
        return actionToReturn;
    }

    protected HashMap<String, Action> actions;
}