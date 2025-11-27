package es.upm.etsisi.poo;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.ProductContainer.Catalog;
import es.upm.etsisi.poo.ProductContainer.Category;
import es.upm.etsisi.poo.Requests.Handlers.CashierHandler;
import es.upm.etsisi.poo.Requests.Handlers.CustomerHandler;
import es.upm.etsisi.poo.Requests.Handlers.ProductHandler;
import es.upm.etsisi.poo.Requests.Handlers.TicketHandler;
import es.upm.etsisi.poo.Requests.Request;
import es.upm.etsisi.poo.Requests.RequestHandler;
import es.upm.etsisi.poo.TicketContainer.TicketBook;
import es.upm.etsisi.poo.UserContainer.Customer;
import es.upm.etsisi.poo.UserContainer.User;
import es.upm.etsisi.poo.UserContainer.UserRegister;

import java.util.HashMap;
import java.util.function.Consumer;

public class App
{
    public Catalog catalog;
    public TicketBook tickets;
    public UserRegister<User> cashiers;
    public UserRegister<Customer> customers;

    App(String[] args)
    {
        catalog = new Catalog();
        tickets = new TicketBook();
        cashiers = new UserRegister();
        customers = new UserRegister();

        initCommandsMap();
        initModulesMap();
    }

    public void init(String inputFile)
    {
        initInput(inputFile);
        printWelcome();

        while (true)
        {
            Request request = input.next();
            if (!request.handlerId.isEmpty())
            {
                handleRequest(request);
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

    private void handleRequest(Request request)
    {
        if (modules.containsKey(request.handlerId))
        {
            modules.get(request.handlerId).getAction(request.actionId).execute(request.args);
        }
        else if (commands.containsKey(request.handlerId))
        {
            commands.get(request.handlerId).accept(request);
        }
        else
        {
            System.err.printf("ERROR: Invalid command %s\n", request.handlerId);
        }
    }

    private void executeAction(Action action) {}

    /**
     * This method prints all the commands with its parameters
     */
    private void help() {
        // Initialize StringBuilder to build the entire output
        StringBuilder output = new StringBuilder();

        // Show the commands
        output.append("Commands:\n");
        for (RequestHandler requestHandler : modules.values()) {
            for (Action action : requestHandler.getActions().values()) {
                output.append(action.help()).append("\n");
            }
        }

        // Show the categories
        output.append("Categories: ").append(Category.getCategories()).append("\n");

        // Show the categories and their discounts
        output.append("Discounts if there are ≥2 units in the category: ")
                .append(Category.getCategoriesWithDiscount()).append("\n\n");

        // Print all the content built in the StringBuilder at the end
        System.out.print(output);
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

    private void initCommandsMap()
    {
        commands.put(BUILTIN_CMD_EXIT, (request) -> exit());
        commands.put(BUILTIN_CMD_HELP, (request) -> help());
        commands.put(BUILTIN_CMD_ECHO, this::echo);
    }

    private void initModulesMap()
    {
        // TODO change keys to something less magical
        modules.put("prod", new ProductHandler(this));
        modules.put("ticket", new TicketHandler(this));
        modules.put("client", new CustomerHandler(this));
        modules.put("cash", new CashierHandler(this));
    }

    private InputDriver input;

    private final HashMap<String, RequestHandler> modules = new HashMap<>();
    private final HashMap<String, Consumer<Request>> commands = new HashMap<>();

    private final String BUILTIN_CMD_EXIT = "exit";
    private final String BUILTIN_CMD_HELP = "help";
    private final String BUILTIN_CMD_ECHO = "echo";
} // class App
