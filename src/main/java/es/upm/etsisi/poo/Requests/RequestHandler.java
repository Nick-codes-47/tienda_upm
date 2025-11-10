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

    public Action getAction(Request request) {
        if (request != null) {
            // We get the action in the request
            String action = request.actionId;
            // We look if we have that action in the map
            Action actionToReturn = this.actions.get(action);
            if (actionToReturn == null) {
                System.err.println("ERROR: No such action: " + action);
            }
            return actionToReturn;
        } else return null;
    }
}