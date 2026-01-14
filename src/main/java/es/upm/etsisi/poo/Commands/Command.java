package es.upm.etsisi.poo.Commands;

import es.upm.etsisi.poo.Models.Core.AppException;

public interface Command {

    int execute(String[] args);

    String help();
}
