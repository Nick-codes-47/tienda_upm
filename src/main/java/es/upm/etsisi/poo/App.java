package es.upm.etsisi.poo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Arrays;
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
                    handleProductOrder(command); // TODO class ProductModule?
                    break;
                }
                case "ticket":
                {
                    handleTicketOrder(command); // TODO class TicketModule?
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

    private Command nextCommand(Scanner input)
    {
        return new Command(input.nextLine().split("\\s+")); // using spaces without living empty Strings
    }

    private void handleProductOrder(Command command)
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

    private void handleTicketOrder(Command command)
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
     * Method that prints the current Ticket with the products and price
     */
    private void printTicket() {
        System.out.println(ticket.toString());
    }

    /**
     * Method that prints the catalog of products
     */
    private void printProdList() {
        System.out.println("Catalog: ");
        for (Product product : products.values()) {
            System.out.println(" "+product.toString());
        }
    }

    /**
     * Method to add a product to the catalog so it is available to buy it
     * @param id to have an identification for the product
     * @param name name of the product
     * @param category category of the product
     * @param price price of the product
     * @return  -2 if we already reached the maxProducts
     *          -1 if the id already exists
     *          0 if the product was added to the catalog without problem
     *          1 if the category doesn't exist
     */
    private int addProduct(int id, String name, String category, double price) {
        // We check if we reached the maxProducts
        if (products.size() >= config.getMaxProducts()) {
            System.out.println("You reached the maximum number of products!");
            return -2;
        }
        // We check if there already is a product with the same id
        if (products.containsKey(id)) {
            System.out.println("Product's id already exists");
            return -1;
        }
        // If the id doesn't exist we check if the category is valid
        if (!config.validCategory(category)) {
            System.out.println("Invalid category!");
            return 1;
        }
        // If everything went well we add the product
        Product newProduct = new Product(category, id, name, price);
        products.put(id, newProduct);
        System.out.println(newProduct);
        return 0;
    }

    /**
     * Method to update the fields of a product (NAME, CATEGORY, PRICE) in the catalog and the ticket
     * @param id to search the product
     * @param field to know which field of the product needs to be changed
     * @param value the new value of the field
     * @return -1 if the product doesn't exist in the catalog.
     *          0 if the field was changed correctly
     *          1 if the product exists but the field doesn't
     *          2 if we don't have permission to change the field (ID)
     */
    private int updateProduct(int id, String field, String value) {
        Product product = products.get(id);
        if (product == null) {
            System.out.println("Product with id " + id + " does not exist!");
            return -1;
        }
        // We try to get the product's field to be modified
        try {
            Field f = Product.class.getDeclaredField(field);
            // We only permit changes on other fields than the id
            if (!field.equalsIgnoreCase("id")) { f.setAccessible(true); }
            // We check if we have to change the price to parse
            if (f.getType().equals(double.class) || f.getType().equals(Double.class)) {
                f.set(product, Double.parseDouble(value));
            } else { f.set(product, value); }
            // We print the product with the new value for the field
            System.out.println(product);
            // We update the product in the ticket also
            if (ticket.updateProduct(product) == 0) {
                // if there was a change in the ticket we show it
                System.out.println("The ticket was also updated!");
                this.printTicket();
            }
            return 0; // if everything went well
        } catch (NoSuchFieldException e) {
            System.out.println("Field not valid!");
            return 1; // if field doesn't exist we return 1
        } catch (IllegalAccessException e) {
            System.out.println("Illegal access! (You can't modify the id)");
            return 2; // Mainly if we try to modify the ID
        }
    }

    /**
     * Method to delete a product from the catalog and the ticket
     * @param id to search the product
     * @return  -1 if the product doesn't exist
     *          0 if we could delete the product
     */
    private int deleteProduct(int id) {
        Product  product = products.get(id);
        if (product == null) {
            System.out.println("Product with id " + id + " does not exist!");
            return -1;
        }
        // If the product exist in the catalog we print it and delete it
        System.out.println("Deleting the product:\n"
                +products.get(id).toString());
        products.remove(id);
        // If the product was in the ticket we also delete it from there and show the change
        if  (ticket.deleteProduct(product) == 0) {
            System.out.println("The ticket was also updated!");
            this.printTicket();
        }
        return 0;
    }

    /**
     * Resets the ticket so it has 0 products
     */
    private void resetTicket() {
        ticket.resetTicket();
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
