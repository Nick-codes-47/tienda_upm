package es.upm.etsisi.poo;

import java.util.ArrayList;

public class App
{
    Ticket currentTicket;
    ArrayList<Product> productList;
    Config config;

    App(String[] args)
    {
        loadConfig(args);
    }

    public void init()
    {
        // TODO
    }

    private void loadConfig(String[] args)
    {
        if (args.length > 1)
        {
            config = new Config(args[1]);
        }
        else
        {
            config = new Config();
        }
    }

    /**
     * @param args
     *      args[1] - config file path
     */
    public static void main(String[] args) {
        try
        {
            App app = new App(args);
            app.init();
        }
        catch (RuntimeException exception) // TODO create AppException?
        {
            System.err.printf("ERROR::main> " + exception);
        }
    }
}
