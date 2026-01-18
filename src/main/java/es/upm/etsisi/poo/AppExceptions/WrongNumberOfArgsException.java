package es.upm.etsisi.poo.AppExceptions;

import es.upm.etsisi.poo.Commands.Command;

public class WrongNumberOfArgsException extends AppException {
    public WrongNumberOfArgsException(Command command){
        super("Wrong number of arguments\n" + ((command != null)? command.help() : ""));
    }
}
