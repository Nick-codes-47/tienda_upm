package es.upm.etsisi.poo.Commands;

public interface Command {

    int execute(String[] args);

    String help();
}
