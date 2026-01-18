package es.upm.etsisi.poo.AppExceptions.ArgumentExceptions;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Commands.Command;

public class WrongCommandArgumentsException extends AppException {
    public WrongCommandArgumentsException(String message, Command command) {
        super("Wrong use of command arguments: " + message + "\n" + command.help());
    }
}
