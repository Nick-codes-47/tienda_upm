package es.upm.etsisi.poo.Actions;

import es.upm.etsisi.poo.App;

public interface Action {

    int execute(String[] args);

    String help();
}
