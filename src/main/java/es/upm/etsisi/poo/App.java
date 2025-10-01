package es.upm.etsisi.poo;

import java.lang.reflect.Field;
import java.util.HashMap;

public class App
{
    Ticket currentTicket;
    HashMap<Integer, Product> products;
    Config config;

    public static void main( String[] args ) {
    }

    public void printTicket() {
        System.out.println(currentTicket.toString());
    }

    public void printProdList() {
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
    public int addProduct(int id, String name, String category, double price) {
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
        products.put(id, new Product(category, id, name, price));
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
    public int updateProduct(int id, String field, String value) {
        Product product = products.get(id);
        if (product == null) { return -1; }
        else {
            // We try to get the product's field to be modified
            try {
                Field f = Product.class.getDeclaredField(field);
                // We don't give the option to change the id
                if (!field.equalsIgnoreCase("id")) { f.setAccessible(true); }
                if (f.getType().equals(double.class) || f.getType().equals(Double.class)) {
                    f.set(product, Double.parseDouble(value));
                } else { f.set(product, value); }
                // We update the product in the ticket also
                currentTicket.updateProduct(product);
                return 0; // if everything went well
            } catch (NoSuchFieldException e) {
                return 1; // if field doesn't exist we return 1
            } catch (IllegalAccessException e) {
                return 2; // Mainly if we try to modify the ID
            }
        }
    }

    /**
     * Method to delete a product from the catalog and the ticket
     * @param id to search the product
     * @return  -1 if the product doesn't exist
     *          0 if we could delete the product
     */
    public int deleteProduct(int id) {
        return 0;
    }
}
