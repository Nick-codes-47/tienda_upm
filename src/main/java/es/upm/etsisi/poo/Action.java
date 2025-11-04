package es.upm.etsisi.poo;

public abstract class Action {
    public int result;
    public App app;

    public Action(App app) {
        this.app = app;
    }

    abstract public int execute(String[] args);

    abstract public void help();

}
