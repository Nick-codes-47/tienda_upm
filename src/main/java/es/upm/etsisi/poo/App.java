package es.upm.etsisi.poo;

import java.util.ArrayList;
import java.util.HashMap;

public class App
{
    Ticket ticket;
    HashMap<Integer, Product> products;
    Config config;

    App(String[] args)
    {
        loadConfig(args);
    }

    public void init()
    {
        CommandParser commandParser = new CommandParser();
        boolean exit = false;

        while (!exit)
        {
            Command command = commandParser.nextCommand();
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

    private void handdleProductOrder(Command command)
    {
        int result;
        switch (command.order)
        {
            case "add":
            {
                if (command.args.length == 4) {
                    result = addProduct(
                            Integer.parseInt(command.args[0]),
                            command.args[1],
                            command.args[2],
                            Double.parseDouble(command.args[3])
                    );
                }
                else
                {
                    result = 1; // TODO print correct use
                }
                break;
            }
            case "update":
            {
                if (command.args.length == 3) {
                    result = updateProduct(
                            Integer.parseInt(command.args[0]),
                            command.args[1],
                            command.args[2]
                    );
                }
                else
                {
                    result = 1; // TODO print correct use
                }
                break;
            }
            case "remove":
            {
                if (command.args.length == 1)
                {
                    result = deleteProduct(Integer.parseInt(command.args[0]));
                }
                else
                {
                    result = 1; // TODO print correct use
                }
                break;
            }
            case "list":
            {
                printProdList();
                break;
            }
            default:
            {
                // TODO print options for prod command
            }
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
}
