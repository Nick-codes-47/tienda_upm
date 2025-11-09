package es.upm.etsisi.poo.ProductContainer;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Requests.Request;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Class that implements a HashMap to store all the products available in the store
 */
public class Catalog {
    private final App app;
    private final HashMap<Integer, BaseProduct> products;
    private final int maxProducts;
    public static final String COMMAND_PREFIX = "prod";

    public Catalog(App app) {
        this.app = app;
        this.maxProducts = app.config.getMaxProducts();
        products = new HashMap<>();
    }

    /**
     * Method to handle requests from the users that involve products and the catalog
     *
     * @param request object request to know which method needs to be executed
     * @return Returns 0 if the execution went as expected;
     *          1 if the arguments are not the necessary;
     *          2 if the arguments are valid
     */
    public int handleRequest(Request request) {
        String command = request.command;
        ArrayList<String> args = request.args;

        switch (command) {
            case "add": {
                if (args.size() != 4) {
                    System.err.println("ERROR: Not the required number of arguments");
                    return 1;
                }
                try {
                    return this.add(
                            Integer.parseInt(args.get(0)), // id
                            args.get(1),                   // name
                            args.get(2),                   // category
                            Double.parseDouble(args.get(3)) // price
                    );
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: id and/or price are not valid");
                    return 2;
                }
            }

            case "list": {
                this.printProdList();
                return 0;
            }

            case "update": {
                if (args.size() != 3) {
                    System.err.println("ERROR: Not the required number of arguments");
                    return 1;
                }
                try {
                    return this.updateProduct(
                            Integer.parseInt(args.get(0)), // id
                            args.get(1),                   // field
                            args.get(2)                    // new value
                    );
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: id is invalid");
                    return 2;
                }
            }

            case "remove": {
                if (args.size() != 1) {
                    System.err.println("ERROR: Not the required number of arguments");
                    return 1;
                }
                try {
                    return this.deleteProduct(Integer.parseInt(args.get(0)));

                } catch (NumberFormatException e) {
                    System.err.println("ERROR: id is invalid");
                    return 2;
                }
            }

            default: {
                System.err.println("ERROR: Invalid command "+command);
                return 3;
            }
        }
    }


    /**
     * Method to search a product in the catalog
     *
     * @param id id of the product to search it
     * @return null if the id is not correct.
     * the product with the id if it's correct
     */
    public BaseProduct getProduct(int id) {
        return products.get(id);
    }

    /**
     * Method that prints the catalog of products
     */
    private void printProdList() {
        if (products.isEmpty()) {
            System.out.println("There are no products in the catalog yet!");
        } else {
            System.out.println("Catalog: ");
            for (BaseProduct product : products.values()) {
                System.out.println(" " + product.toString());
            }
        }
    }

    /**
     * Method to add a product to the catalog so it is available to buy it
     *
     * @param product the product that needs to be added to the catalog
     * @return 0 if the product was added to the catalog without problem;
     *         -2 if the product passed was a null;
     *         -1 if we already reached the maxProducts;
     */
    public int add(BaseProduct product) {
        // Discard null objects
        if (product == null) {
            System.err.println("ERROR: Product is null");
            return -2;
        }
        // We check if we reached the maxProducts
        if (products.size() >= maxProducts) {
            System.err.println("ERROR: You reached the maximum number of products!");
            return -1;
        }
        // Put the product in the map and print it
        products.put(product.getId(), product);
        return 0;
    }

    public int getNewId() {
        Set<Integer> usedIds = products.keySet();
        int newId = 0;
        while (usedIds.contains(newId)) {
            newId++;
        }
        return newId;
    }

    /**
     * Method to delete a product from the catalog and the ticket
     *
     * @param id to search the product
     * @return -1 if the product doesn't exist
     * 0 if we could delete the product
     */
    public BaseProduct deleteProduct(int id) {
        BaseProduct product = this.getProduct(id);
        if (product != null) {
            // If the product exist in the catalog we delete it
            products.remove(id);
        }
        return product;
    }
}
