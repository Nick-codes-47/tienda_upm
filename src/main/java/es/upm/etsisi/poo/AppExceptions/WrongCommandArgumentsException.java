package es.upm.etsisi.poo.AppExceptions;

import es.upm.etsisi.poo.Commands.Command;

public class WrongCommandArgumentsException extends AppException {
    public WrongCommandArgumentsException(String message, Command command) {
        super("Wrong use of command arguments: " + message + "\n" + command.help());
    }
}
