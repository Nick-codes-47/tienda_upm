package es.upm.etsisi.poo;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

public class App
{
    public Config config;

    App(String[] args)
    {
        loadConfig(args);
        initCommandMaps();
        catalog = new Catalog(this);
        ticket = new Ticket(this);
    }

    public void init()
    {
        InputDriver input = new InputDriver();
        boolean exit = false;

        while (!exit)
        {
            Request request = input.nextRequest();
            if (builtinCommands.containsKey(request.family))
            {
                builtinCommands.get(request.command).run();
            }
            else if (moduleHandlers.containsKey(request.family))
            {
                moduleHandlers.get(request.family).accept(request);
            }

            if (Objects.equals(request.family, "exit"))
           {
               exit = true;
           }
        }
    }

    public Product getProduct(int id)
    {
        return catalog.getProduct(id);
    }

    /**
     * @param args
     *      args[1] - input file path
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

    private void loadConfig(String[] args)
    {
        if (args.length > 2)
        {
            config = new Config(args[2]);
        }
        else
        {
            config = new Config();
        }
    }

    /**
     * This method prints all the commands with its parameters
     */
    public void help() {
        System.out.println("Commands:\n" +
                " prod add <id> \"<name>\" <category> <price>\n" +
                " prod list\n" +
                " prod update <id> NAME|CATEGORY|PRICE <value>\n" +
                " prod remove <id>\n" +
                " ticket new\n" +
                " ticket add <prodId> <quantity>\n" +
                " ticket remove <prodId>\n" +
                " ticket print\n" +
                " echo \"<texto>\"\n" +
                " help\n" +
                " exit");
    }

    /**
     * Method to exit the program's execution
     */
    public void exit() {
        System.out.println("Closing Application.\n" +
                "Goodbye!");
        System.exit(0);
    }

    private void initCommandMaps()
    {
        initBuiltinCommandMap();
        initModuleCommandMap();
    }

    private void initBuiltinCommandMap()
    {
        builtinCommands.put("exit", this::exit);
        builtinCommands.put("help", this::help);
    }

    private void initModuleCommandMap()
    {
        moduleHandlers.put("prod", (request) -> catalog.handleRequest(request));
        moduleHandlers.put("ticket", (request) -> ticket.handleRequest(request));
    }

    private Catalog catalog;
    private Ticket ticket;

    private HashMap<String, Runnable> builtinCommands;
    private HashMap<String, Consumer<Request>> moduleHandlers;
} // class App
