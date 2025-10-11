package es.upm.etsisi.poo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that implements a HashMap to store all the products available in the store
 */
public class Catalog {
    private final App app;
    private final HashMap<Integer, Product> products;
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
     */
    public void handleRequest(Request request) {
        String command = request.command;
        ArrayList<String> args = request.args;

        switch (command) {
            case "add": {
                if (args.size() != 4) {
                    System.out.println("Not the required number of arguments");
                    break;
                }
                try {
                    this.addProduct(
                            Integer.parseInt(args.get(0)), // id
                            args.get(1),                   // name
                            args.get(2),                   // category
                            Double.parseDouble(args.get(3)) // price
                    );
                } catch (NumberFormatException e) {
                    System.out.println("id and/or price are not valid");
                }
                break;
            }

            case "list": {
                this.printProdList();
                break;
            }

            case "update": {
                if (args.size() != 3) {
                    System.out.println("Not the required number of arguments");
                    break;
                }
                try {
                    this.updateProduct(
                            Integer.parseInt(args.get(0)), // id
                            args.get(1),                   // field
                            args.get(2)                    // new value
                    );
                } catch (NumberFormatException e) {
                    System.out.println("id is invalid");
                }
                break;
            }

            case "remove": {
                if (args.size() != 1) {
                    System.out.println("Not the required number of arguments");
                    break;
                }
                try {
                    // We also delete it from the ticket
                    this.deleteProduct(Integer.parseInt(args.get(0)));
                    app.ticket.handleRequest(request);

                } catch (NumberFormatException e) {
                    System.out.println("id is invalid");
                }
                break;
            }

            default: {
                System.out.println("Invalid command");
                break;
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
    public Product getProduct(int id) {
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
            for (Product product : products.values()) {
                System.out.println(" " + product.toString());
            }
        }
    }

    /**
     * Method to add a product to the catalog so it is available to buy it
     *
     * @param id       to have an identification for the product
     * @param name     name of the product
     * @param category category of the product
     * @param price    price of the product
     * @return -3 if id or price are negative
     * -2 if we already reached the maxProducts
     * -1 if the id already exists
     * 0 if the product was added to the catalog without problem
     * 1 if the category doesn't exist
     */
    private int addProduct(int id, String name, String category, double price) {
        // We check if we reached the maxProducts
        if (products.size() >= maxProducts) {
            System.err.println("ERROR: You reached the maximum number of products!");
            return -2;
        }
        // We check if there already is a product with the same id
        if (products.containsKey(id)) {
            System.err.println("ERROR: Product's id already exists");
            return -1;
        }
        // If the id does exist we check if the category is valid
        if (!app.config.validCategory(category)) {
            System.err.println("ERROR: Invalid category!");
            return 1;
        }
        // If the name is longer than 100 chars its invalid
        if (name.length() > 100) {
            System.err.println("ERROR: Product name is too long. Max 100 characters");
            return 1;
        }
        // ID must be positive
        if (id < 0) {
            System.err.println("ERROR: Product ID must be positive");
            return -3;
        }
        // Price must be positive
        if (price < 0) {
            System.err.println("ERROR: Product price must be positive");
            return -3;
        }
        // If everything went well we add the product
        Product newProduct = new Product(category, id, name, price);
        products.put(id, newProduct);
        System.out.println(newProduct);
        return 0;
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
            System.out.println("Product with id " + id + " does not exist!");
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
            System.out.println(product);
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
     *
     * @param id to search the product
     * @return -1 if the product doesn't exist
     * 0 if we could delete the product
     */
    private int deleteProduct(int id) {
        Product product = this.getProduct(id);
        if (product == null) {
            System.out.println("Product with id " + id + " does not exist!");
            return -1;
        }
        // If the product exist in the catalog we print it and delete it
        System.out.println(product);
        products.remove(id);
        return 0;
    }
}
