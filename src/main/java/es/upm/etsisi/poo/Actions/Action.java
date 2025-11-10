package es.upm.etsisi.poo.Actions;

import es.upm.etsisi.poo.App;

public abstract class Action {
    public App app;

    public Action(App app) {
        this.app = app;
    }

    abstract public int execute(String[] args);

    abstract public void help();
}
