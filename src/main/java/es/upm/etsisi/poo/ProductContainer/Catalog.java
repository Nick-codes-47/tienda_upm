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
     * Method to update the fields of a product (NAME, CATEGORY, PRICE) in the catalog and the ticket
     *
     * @param id    to search the product
     * @param field to know which field of the product needs to be changed
     * @param value the new value of the field
     * @return -1 if the product doesn't exist in the catalog.
     * 0 if the field was changed correctly
     * 1 if the product exists but the field doesn't
     * 2 if we don't have permission to change the field (ID)
     */
    private int updateProduct(int id, String field, String value) {
        Product product = this.getProduct(id);
        if (product == null) {
            System.err.println("ERROR: Product with id " + id + " does not exist!");
            return -1;
        }
        // We try to get the product's field to be modified
        try {
            Field f = Product.class.getDeclaredField(field.toLowerCase()); // TODO be careful if Products variable are written in camelCase
            // We only permit changes on other fields different from the id
            if (!field.equalsIgnoreCase("id")) {
                f.setAccessible(true);
            }
            // We check if we have to change the price to parse
            if (f.getType().equals(double.class) || f.getType().equals(Double.class)) {
                f.set(product, Double.parseDouble(value));
            } else {
                f.set(product, value);
            }
            // We print the product with the new value for the field
            System.out.println(product+"\n");
            // We tell ticket that a product was updated
            app.updateProduct(product);
            return 0; // if everything went well
        } catch (NoSuchFieldException e) {
            System.err.println("ERROR: Field not valid!");
            return 1; // if field doesn't exist we return 1
        } catch (IllegalAccessException e) {
            System.err.println("ERROR: Illegal access! (You can't modify the id)");
            return 2; // Mainly if we try to modify the ID
        }
    }

    /**
     * Method to delete a product from the catalog and the ticket
     *
     * @param id to search the product
     * @return -1 if the product doesn't exist
     * 0 if we could delete the product
     */
    private int deleteProduct(int id) {
        Product product = this.getProduct(id);
        if (product == null) {
            System.err.println("ERROR: Product with id " + id + " does not exist!");
            return -1;
        }
        // If the product exist in the catalog we print it and delete it
        System.out.println(product+"\n");
        products.remove(id);
        //We tell the ticket the removed product
        app.deleteProduct(product);
        return 0;
    }
}
