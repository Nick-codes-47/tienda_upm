package es.upm.etsisi.poo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class App
{
    public Config config;

    App(String[] args)
    {
        loadConfig(args);
    }

    public void init()
    {
        Scanner input = new Scanner(System.in);
        boolean exit = false;

        while (!exit)
        {
            System.out.print(PROMPT);
            Command command = nextCommand(input);
            switch (command.family) // Change to command map
            {
                case "prod":
                {
                    handdleProductOrder(command); // TODO class ProductModule?
                    break;
                }
                case "ticket":
                {
                    handdleTicketOrder(command); // TODO class TicketModule?
                    break;
                }
                case "help":
                {
                    help();
                    break;
                }
                case "exit":
                {
                    exit();
                    exit = true;
                    break;
                }
                default:
                {

                }
            }
        }
    }

    private Command nextCommand(Scanner input)
    {
        return new Command(input.nextLine().split("\\s+")); // using spaces without living empty Strings
    }

    private void handdleProductOrder(Command command)
    {
        int result = 0;

        if (command.order != null) {
            switch (command.order) {
                case "add": {
                    if (command.args.length == 4) {
                        result = addProduct(
                                Integer.parseInt(command.args[0]),
                                command.args[1],
                                command.args[2],
                                Double.parseDouble(command.args[3])
                        );
                    } else {
                        result = 1;
                    }
                    break;
                }
                case "update": {
                    if (command.args.length == 3) {
                        result = updateProduct(
                                Integer.parseInt(command.args[0]),
                                command.args[1],
                                command.args[2]
                        );
                    } else {
                        result = 1;
                    }
                    break;
                }
                case "remove": {
                    if (command.args.length == 1) {
                        result = deleteProduct(Integer.parseInt(command.args[0]));
                    } else {
                        result = 1;
                    }
                    break;
                }
                case "list": {
                    printProdList();
                    break;
                }
            }
        }
        else
        {
            result = 1;
        }

        if (result == 1)
        {
            // TODO print correct use
        }
    }

    private int addProduct(int id, String name, String category, double price)
    {
        return 0;
    }

    private int updateProduct(int id, String field, String value)
    {
        return 0;
    }

    private int deleteProduct(int id)
    {
        return 0;
    }

    private void printProdList()
    {

    }

    private void handdleTicketOrder(Command command)
    {
        switch (command.order)
        {
            case "new":
            {
                resetTicket();
                break;
            }
            case "add":
            {
                ticket.addProduct(
                        products.get(Integer.parseInt(command.args[0])),
                        Integer.parseInt(command.args[1])
                );
                break;
            }
            case "remove":
            {
                ticket.deleteProduct(products.get(Integer.parseInt(command.args[0])));
                break;
            }
            case "print":
            {
                printTicket();
                break;
            }
        }
    }

    private void printTicket()
    {

    }

    private void resetTicket()
    {

    }

    private void help()
    {

    }

    private void exit()
    {

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

    private class Command
    {
        public String family;
        public String order;
        public String[] args;

        public Command(String[] tokens)
        {
            family = tokens[0];
            if (tokens.length > 1)
            {
                order = tokens[1];
                args = Arrays.copyOfRange(tokens, 2, tokens.length);
            }

            System.err.printf("LOG::Command> command line received: %s %s %s\n", family, order, Arrays.toString(args));
        }
    }

    private Ticket ticket;
    private HashMap<Integer, Product> products;
    private static final String PROMPT = "tUPM> ";
} // class App
