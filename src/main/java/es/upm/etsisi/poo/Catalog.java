package es.upm.etsisi.poo;

import java.lang.reflect.Field;
import java.util.HashMap;

public class Catalog {
    private App app;
    private HashMap<Integer, Product> products;
    private int maxProducts;

    public Catalog(App app) {
        this.app = app;
        this.maxProducts = app.config.getMaxProducts();
    }

    public int handleRequest (Request request) {
        return 0;
    }

    public Product getProduct(int id) {
        return products.get(id);
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
        if (products.size() >= maxProducts) {
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
}
