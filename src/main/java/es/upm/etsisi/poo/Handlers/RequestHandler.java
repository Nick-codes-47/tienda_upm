package es.upm.etsisi.poo.Handlers;

import es.upm.etsisi.poo.Commands.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public abstract class RequestHandler {
    public final String HANDLER_ID;

    public RequestHandler(String handlerId) {
        HANDLER_ID = handlerId;
        commands = new HashMap<>();
    }

    public List<Command> getCommands() {
        List<Command> commands = new ArrayList<>();

        for (Supplier<Command> command : this.commands.values()) {
            commands.add(command.get());
        }

        return commands;
    }

    public Command getCommand(String commandID) {
        // We look if we have that action in the map
        Supplier<Command> command = this.commands.get(commandID);
        if (command == null) {
            System.err.println("ERROR: No such action: " + commandID);
            return null;
        }
        return command.get();
    }

    protected HashMap<String, Supplier<Command>> commands;
}