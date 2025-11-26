package es.upm.etsisi.poo.Requests;

import es.upm.etsisi.poo.Actions.Action;

import java.util.HashMap;

public class RequestHandler {
    private HashMap<String, Action> actions;

    public RequestHandler() {
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
}