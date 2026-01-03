package es.upm.etsisi.poo;

import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Containers.Product.Catalog;
import es.upm.etsisi.poo.Containers.Product.ProductTypes.ProductEnums.Category;
import es.upm.etsisi.poo.Containers.User.CashierRegister;
import es.upm.etsisi.poo.Containers.User.CustomerRegister;
import es.upm.etsisi.poo.Handlers.CashierHandler;
import es.upm.etsisi.poo.Handlers.CustomerHandler;
import es.upm.etsisi.poo.Handlers.ProductHandler;
import es.upm.etsisi.poo.Handlers.TicketHandler;
import es.upm.etsisi.poo.Handlers.Request;
import es.upm.etsisi.poo.Handlers.RequestHandler;
import es.upm.etsisi.poo.Services.TicketService;

import java.util.HashMap;
import java.util.function.Consumer;

public class App {

    public final Catalog catalog = new Catalog();
    public final CashierRegister cashiers = new CashierRegister();
    public final CustomerRegister customers = new CustomerRegister();

    public final TicketService ticketService = new TicketService(cashiers);

    public App() {}

    public void init(String inputFile) {
        initBuiltinCommandsMap();
        initHandlersMap();

        initInput(inputFile);
        printWelcome();

        while (true) {
            Request request = input.next();
            if (!request.handlerId.isEmpty()) {
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
        try {
            App app = new App();

            if (args.length > 0) {
                app.init(args[0]);
            }
            else {
                app.init(null);
            }
        }
        catch (RuntimeException exception) {
            System.err.printf("ERROR::main> " + exception);
        }
    }

    private void printWelcome() {
        System.out.println("Welcome to the ticket module App.\n" +
                "Ticket module. Type 'help' to see commands.");
    }

    private void initInput(String inputFile) {
        if (inputFile != null) {
            input = new InputDriver(inputFile);
        }
        else {
            input = new InputDriver();
        }
    }

    private void handleRequest(Request request) {
        if (handlerIds.containsKey(request.handlerId)) {
            execute(handlers[handlerIds.get(request.handlerId)].getAction(request.commandId), request);
        }
        else if (builtinCommands.containsKey(request.handlerId)) {
            builtinCommands.get(request.handlerId).accept(request);
        }
        else {
            System.err.printf("ERROR: Invalid command %s\n", request.handlerId);
        }
    }

    private void execute(Command command, Request request) {
         if (command == null)
            return;
        
        int retVal = command.execute(request.args);
        if (retVal == 0)
            System.out.printf("%s %s: ok\n", request.handlerId, request.commandId);
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
//            for (Command command : requestHandler.getCommand().values()) {
//                output.append(String.format("  %s %s\n", requestHandler.HANDLER_ID, command.help()));
//            } // TODO
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
        System.out.println(request.commandId);
    }

    private void initHandlersMap() {
        handlers = new RequestHandler[] {
                new CustomerHandler(this),
                new CashierHandler(this),
                new TicketHandler(this),
                new ProductHandler(this)
        };

        for (int i = 0; i < handlers.length; i++) {
            handlerIds.put(handlers[i].HANDLER_ID, i);
        }
    }

    private void initBuiltinCommandsMap() {
        builtinCommands.put("help", (request) -> help());
        builtinCommands.put("echo", this::echo);
        builtinCommands.put("exit", (request) -> exit());
    }

    // This is done with separate structures instead of a single Hashmap<String, RequestHandler>
    // in order to maintain the order of RequestsHandlers so functions output like help respect the order in the
    // handlers array
    private RequestHandler[] handlers;
    private final HashMap<String, Integer> handlerIds = new HashMap<>();

    private final HashMap<String, Consumer<Request>> builtinCommands = new HashMap<>();

    private InputDriver input;
} // class App
