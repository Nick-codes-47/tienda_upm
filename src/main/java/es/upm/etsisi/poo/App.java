package es.upm.etsisi.poo;

import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.Category;
import es.upm.etsisi.poo.Models.User.CashierRegister;
import es.upm.etsisi.poo.Models.User.CustomerRegister;
import es.upm.etsisi.poo.Models.User.Users.Cashier;
import es.upm.etsisi.poo.Models.User.Users.Customer;
import es.upm.etsisi.poo.Handlers.CashierHandler;
import es.upm.etsisi.poo.Handlers.CustomerHandler;
import es.upm.etsisi.poo.Handlers.ProductHandler;
import es.upm.etsisi.poo.Handlers.TicketHandler;
import es.upm.etsisi.poo.Handlers.Request;
import es.upm.etsisi.poo.Handlers.RequestHandler;
import es.upm.etsisi.poo.Persistence.PersistenceService; // <--- IMPORTANTE
import es.upm.etsisi.poo.Services.TicketService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.function.Consumer;

public class App {

    public final Catalog catalog = new Catalog();
    public final CashierRegister cashiers = new CashierRegister();
    public final CustomerRegister customers = new CustomerRegister();

    private final PersistenceService persistence = new PersistenceService();

    public final TicketService ticketService = new TicketService(cashiers);

    public App() {}

    @SuppressWarnings("unchecked")
    public void init(String inputFile) {
        System.out.println("Loading data...");
        Object[] data = persistence.loadAll();

        if (data[0] != null) catalog.loadData((HashMap<ProductID, BaseProduct>) data[0]);
        if (data[1] != null) customers.loadData((HashMap<String, Customer>) data[1]);
        if (data[2] != null) cashiers.loadData((HashMap<String, Cashier>) data[2]);

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

    public static void main(String[] args) {
        try {
            App app = new App();
            if (args.length > 0) {
                app.init(args[0]);
            } else {
                app.init(null);
            }
        } catch (RuntimeException exception) {
            AppLogger.error(String.format("ERROR::main> %s", exception));
        }
    }

    private void printWelcome() {
        System.out.println("Welcome to the ticket module App.\n" +
                "Ticket module. Type 'help' to see commands.");
    }

    private void initInput(String inputFile) {
        if (inputFile != null) {
            input = new InputDriver(inputFile);
        } else {
            input = new InputDriver();
        }
    }

    private void handleRequest(Request request) {
        if (handlerIds.containsKey(request.handlerId)) {
            execute(handlers[handlerIds.get(request.handlerId)].getCommand(request.commandId), request);
        } else if (builtinCommands.containsKey(request.handlerId)) {
            builtinCommands.get(request.handlerId).accept(request);
        } else {
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

    private void help() {
        StringBuilder output = new StringBuilder();
        output.append("Commands:\n");
        for (RequestHandler requestHandler : handlers) {
            for (Command command : requestHandler.getCommands()) {
                output.append(String.format("  %s %s\n", requestHandler.HANDLER_ID, command.help()));
            }
        }
        output.append("Categories: ").append(Category.getCategories()).append("\n");
        output.append("Discounts if there are ≥2 units in the category: ")
                .append(Category.getCategoriesWithDiscount()).append("\n\n");
        System.out.print(output);
    }

    /**
     * Method to exit the program's execution
     */
    private void exit() {
        System.out.println("Saving data...");
        persistence.saveAll(
                catalog.getProducts(),
                customers.getRawMap(),
                cashiers.getRawMap()
        );

        System.out.println("Closing Application.\nGoodbye!");
        System.exit(0);
    }

    private void echo(Request request) {
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

    private RequestHandler[] handlers;
    private final HashMap<String, Integer> handlerIds = new HashMap<>();
    private final HashMap<String, Consumer<Request>> builtinCommands = new HashMap<>();
    private InputDriver input;
}