package es.upm.etsisi.poo;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.ProductContainer.Catalog;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.ProductEnums.Category;
import es.upm.etsisi.poo.Requests.Handlers.CashierHandler;
import es.upm.etsisi.poo.Requests.Handlers.CustomerHandler;
import es.upm.etsisi.poo.Requests.Handlers.ProductHandler;
import es.upm.etsisi.poo.Requests.Handlers.TicketHandler;
import es.upm.etsisi.poo.Requests.Request;
import es.upm.etsisi.poo.Requests.RequestHandler;
import es.upm.etsisi.poo.TicketContainer.TicketBook;
import es.upm.etsisi.poo.UserContainer.*;

import java.util.HashMap;
import java.util.function.Consumer;

public class App
{
    private static App instance = null;

    public Catalog catalog = new Catalog();
    public TicketBook tickets = new TicketBook();
    public CashierRegister cashiers = new CashierRegister();
    public CustomerRegister customers = new CustomerRegister();

    public static App getInstance() {
        if (instance == null)
            instance = new App();

        return instance;
    }

    private App() {}

    public void init(String inputFile)
    {
        initCommandsMap();
        initModulesMap();

        initInput(inputFile);
        printWelcome();

        while (true) {
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
            App app = App.getInstance();
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
        if (handlerIds.containsKey(request.handlerId)) {
            executeAction(handlers[handlerIds.get(request.handlerId)].getAction(request.actionId), request);
        }
        else if (commands.containsKey(request.handlerId)) {
            commands.get(request.handlerId).accept(request);
        }
        else {
            System.err.printf("ERROR: Invalid command %s\n", request.handlerId);
        }
    }

    private void executeAction(Action action, Request request) {
         if (action == null)
            return;
        
        int retVal = action.execute(request.args);
        if (retVal == 0)
            System.out.printf("%s %s: ok\n", request.handlerId, request.actionId);
    }

    /**
     * This method prints all the commands with its parameters
     */
    private void help() {
        // Initialize StringBuilder to build the entire output
        StringBuilder output = new StringBuilder();

        // Show the commands
        output.append("Commands:\n");
        for (RequestHandler requestHandler : handlers) {
            for (Action action : requestHandler.getActions().values()) {
                output.append(String.format("  %s %s\n", requestHandler.HANDLER_ID, action.help()));
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
        System.out.println(request.actionId);
    }

    // This is done with separate structures instaead of a single Hasmap<String, RequestHandler>
    // in order to maintain the order of RequetsHandlers so functions output like help respect the order in the
    // handlers array
    private RequestHandler[] handlers;
    private final HashMap<String, Integer> handlerIds = new HashMap<>();
    private void initModulesMap() {
        handlers = new RequestHandler[] {
                new CustomerHandler(),
                new CashierHandler(),
                new TicketHandler(),
                new ProductHandler()
        };

        for (int i = 0; i < handlers.length; i++) {
            handlerIds.put(handlers[i].HANDLER_ID, i);
        }
    }

    private final HashMap<String, Consumer<Request>> commands = new HashMap<>();
    private void initCommandsMap() {
        commands.put("help", (request) -> help());
        commands.put("echo", this::echo);
        commands.put("exit", (request) -> exit());
    }

    private InputDriver input;
} // class App
