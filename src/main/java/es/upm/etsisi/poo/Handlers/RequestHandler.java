package es.upm.etsisi.poo.Handlers;

import es.upm.etsisi.poo.Commands.Command;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class RequestHandler {
    public final String HANDLER_ID;

    public RequestHandler(String handlerId) {
        HANDLER_ID = handlerId;
        commands = new HashMap<>();
    }

    public HashMap<String, Supplier<Command>> getCommand() {
        return commands;
    }

    public Command getAction(String commandID) {
        // We look if we have that action in the map
        Command command = this.commands.get(commandID).get();
        if (command == null) {
            System.err.println("ERROR: No such action: " + commandID);
        }
        return command;
    }

    protected HashMap<String, Supplier<Command>> commands;
}