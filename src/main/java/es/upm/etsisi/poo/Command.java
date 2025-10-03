package es.upm.etsisi.poo;

public class Command {

    public String family;
    public String order;
    public String[] args;

    public Command(String family, String order, String[] args)
    {
        this.family = family;
        this.order = order;
        this.args = args;
    }
}
