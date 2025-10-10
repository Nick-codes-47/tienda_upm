package es.upm.etsisi.poo;

import java.util.HashMap;
import java.util.function.Consumer;

public class App
{
    public Config config;
    public final Ticket ticket;

    App(String[] args)
    {
        loadConfig(args);

        catalog = new Catalog(this);
        ticket = new Ticket(this);

        initCommandMaps();
    }

    public void init(String inputFile)
    {
        InputDriver input;

        if (inputFile != null)
        {
            input = new InputDriver(inputFile);
        }
        else
        {
            input = new InputDriver();
        }

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
        if (request.family.equals(BUILTIN_CMD_EXIT))
        {
            return 1;
        }

        if (commands.containsKey(request.family))
        {
            commands.get(request.family).accept(request);
        }
        else
        {
            System.err.printf("Invalid command %s", request.family);
        }

        return 0;
    }

    /**
     * This method prints all the commands with its parameters
     */
    private void help() {
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
    private void exit() {
        System.out.println("Closing Application.\n" +
                "Goodbye!");
        System.exit(0);
    }

    private void echo(Request request)
    {
        System.out.println(request.family + " " + request.command);
    }

    private void initCommandMaps()
    {
        commands.put(Catalog.COMMAND_PREFIX, catalog::handleRequest);
        commands.put(Ticket.COMMAND_PREFIX, ticket::handleRequest);
        commands.put(BUILTIN_CMD_EXIT, (request) -> exit());
        commands.put(BUILTIN_CMD_HELP, (request) -> help());
        commands.put(BUILTIN_CMD_ECHO, this::echo);
    }

    private final Catalog catalog;

    private final HashMap<String, Consumer<Request>> commands = new HashMap<>();

    private final String BUILTIN_CMD_EXIT = "exit";
    private final String BUILTIN_CMD_HELP = "help";
    private final String BUILTIN_CMD_ECHO = "echo";
} // class App
