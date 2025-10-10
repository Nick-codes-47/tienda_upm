package es.upm.etsisi.poo;

import java.util.HashMap;
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

        while (handleRequest(input.nextRequest()) == 0);
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

    private int handleRequest(Request request)
    {
        if (Objects.equals(request.family, BUILTIN_CMD_EXIT))
        {
            return 1;
        }

        if (builtinCommands.containsKey(request.family))
        {
            builtinCommands.get(request.command).run();
        }
        else if (moduleHandlers.containsKey(request.family))
        {
            moduleHandlers.get(request.family).accept(request);
        }

        return 0;
    }

    /**
     * This method prints all the commands with its parameters
     */
    public void help() {
        String commands = "Commands:\n" +
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
                " exit\n\n";
        StringBuilder categories = new StringBuilder("Categories: ");
        for (String category : config.getCategories()) {
            categories.append(category.toUpperCase()).append(", ");
        }
        // We delete the last coma
        categories.deleteCharAt(categories.length() - 2);
        // We build a string for the discounts
        StringBuilder catDiscounts = new StringBuilder("Discounts if there are ≥2 units in the category: ");
        for (String category : config.getCategories()) {
            catDiscounts.append(category.toUpperCase()).append(" ")
                    .append(String.format("%.0f",config.getDiscount(category) * 100)).append("%").append(", ");
        }
        catDiscounts.deleteCharAt(catDiscounts.length() - 2);
        catDiscounts.append(".");
        System.out.println(commands+categories+"\n"+catDiscounts+"\n");
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
        builtinCommands.put(BUILTIN_CMD_EXIT, this::exit);
        builtinCommands.put(BUILTIN_CMD_HELP, this::help);
    }

    private void initModuleCommandMap()
    {
        moduleHandlers.put("prod", (request) -> catalog.handleRequest(request)); // change magic literals to an attr from each module
        moduleHandlers.put("ticket", (request) -> ticket.handleRequest(request));
    }

    private final Catalog catalog;
    private final Ticket ticket;

    private final HashMap<String, Runnable> builtinCommands = new HashMap<>();
    private final HashMap<String, Consumer<Request>> moduleHandlers = new HashMap<>();

    private final String BUILTIN_CMD_EXIT = "exit";
    private final String BUILTIN_CMD_HELP = "help";
} // class App
