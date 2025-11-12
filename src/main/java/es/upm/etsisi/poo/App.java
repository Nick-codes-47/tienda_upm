package es.upm.etsisi.poo;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.ProductContainer.Catalog;
import es.upm.etsisi.poo.ProductContainer.Category;
import es.upm.etsisi.poo.Requests.Request;
import es.upm.etsisi.poo.Requests.RequestHandler;
import es.upm.etsisi.poo.TicketContainer.Ticket;
import es.upm.etsisi.poo.TicketContainer.TicketBook;
import es.upm.etsisi.poo.UserContainer.UserRegister;

import java.util.HashMap;
import java.util.function.Consumer;

public class App
{
    public Catalog catalog;
    public TicketBook tickets;
    public UserRegister cashiers;
    public UserRegister customers;
    public Config config;

    App(String[] args)
    {
        loadConfig(args);

        catalog = new Catalog(this);
        tickets = new TicketBook(this);

        initCommandMaps();
    }

    public void init(String inputFile)
    {
        int exit = 0;

        initInput(inputFile);
        printWelcome();

        while (exit == 0)
        {
            Request request = input.next();
            if (!request.handlerId.isEmpty())
            {
                exit = handleRequest(request);
                System.out.println();
            }
        }
    }

    /**
     * @param args
     *      args[1] - input file path
     */
    public static void main(String[] args) {
        try
        {
            App app = new App(args);

            if (args.length > 0)
            {
                app.init(args[0]);
            }
            else
            {
                app.init(null);
            }
        }
        catch (RuntimeException exception) // TODO create AppException?
        {
            System.err.printf("ERROR::main> " + exception);
        }
    }

    private void printWelcome()
    {
        System.out.println("Welcome to the ticket module App.\n" +
                "Ticket module. Type 'help' to see commands.");
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

    private void initInput(String inputFile)
    {
        if (inputFile != null)
        {
            input = new InputDriver(inputFile);
        }
        else
        {
            input = new InputDriver();
        }
    }

    private int handleRequest(Request request)
    {
        if (commands.containsKey(request.handlerId))
        {
            commands.get(request.handlerId).accept(request);
        }
        else
        {
            System.err.printf("ERROR: Invalid command %s\n", request.handlerId);
        }

        if (request.handlerId.equals(BUILTIN_CMD_EXIT))
        {
            return 1;
        }

        return 0;
    }

    private void executeAction(Action action) {}

    /**
     * This method prints all the commands with its parameters
     */
    private void help() {
        // Show the commands
        System.out.println("Commands:");
        for (RequestHandler requestHandler : modules.values()) {
            for (Action action : requestHandler.getActions().values()) {
                System.out.println(action.help());
            }
        }

        // Show the categories
        System.out.println("Categories: "+Category.getCategories());
        // Show the categories and there discounts
        System.out.println("Discounts if there are ≥2 units in the category: "+Category.getCategoriesWithDiscount());
    }

    /**
     * Method to exit the program's execution
     */
    private void exit() {
        System.out.println("Closing Application.\n" +
                "Goodbye!");
        System.exit(0);
    }

    private void echo(Request request)
    {
        System.out.println(request.handlerId + " " + request.actionId);
    }

    private void acknowledgeResult(int result, Request request)
    {
        if (result == 0)
        {
            System.out.println(request.handlerId + " " + request.actionId + ": ok");
        }
    }

    private void initCommandMaps()
    {
        commands.put(Catalog.COMMAND_PREFIX, (request) -> acknowledgeResult(catalog.handleRequest(request), request));
        commands.put(Ticket.COMMAND_PREFIX, (request) -> acknowledgeResult(ticket.handleRequest(request), request));
        commands.put(BUILTIN_CMD_EXIT, (request) -> exit());
        commands.put(BUILTIN_CMD_HELP, (request) -> help());
        commands.put(BUILTIN_CMD_ECHO, this::echo);
    }

    private InputDriver input;

    private final HashMap<String, RequestHandler> modules = new HashMap<>();
    private final HashMap<String, Consumer<Request>> commands = new HashMap<>();

    private final String BUILTIN_CMD_EXIT = "exit";
    private final String BUILTIN_CMD_HELP = "help";
    private final String BUILTIN_CMD_ECHO = "echo";
} // class App
