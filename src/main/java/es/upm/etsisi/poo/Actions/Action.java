package es.upm.etsisi.poo.Actions;

import es.upm.etsisi.poo.App;

public interface Action {

    abstract public int execute(String[] args);

    abstract public String help();
}
