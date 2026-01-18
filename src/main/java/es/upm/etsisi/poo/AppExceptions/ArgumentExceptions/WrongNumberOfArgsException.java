package es.upm.etsisi.poo.AppExceptions.ArgumentExceptions;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Commands.Command;

public class WrongNumberOfArgsException extends AppException {
    public WrongNumberOfArgsException(Command command){
        super("Wrong number of arguments\n" + ((command != null)? command.help() : ""));
    }
}
