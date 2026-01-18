package es.upm.etsisi.poo.Commands;

import es.upm.etsisi.poo.AppExceptions.AppException;

public interface Command {

    void execute(String[] args) throws AppException;

    String help();
}
