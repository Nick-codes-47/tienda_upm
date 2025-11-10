package es.upm.etsisi.poo.Requests;

import es.upm.etsisi.poo.Actions.Action;

import java.util.HashMap;

abstract class RequestHandler {
    private HashMap<String, Action> actions;

    public RequestHandler() {
        actions = new HashMap<>();
    }

    abstract Action getAction(Request request);
}