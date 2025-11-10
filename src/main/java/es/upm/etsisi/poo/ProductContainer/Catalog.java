package es.upm.etsisi.poo.ProductContainer;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Requests.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Class that implements a HashMap to store all the products available in the store
 */
public class Catalog {
    private final HashMap<Integer, BaseProduct> products;
    private final int maxProducts;
    public static final String COMMAND_PREFIX = "prod";

    public Catalog(App app) {
        this.maxProducts = app.config.getMaxProducts();
        products = new HashMap<>();
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

    public HashMap<Integer, BaseProduct> getProducts() { return products; }

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
            return -2;
        }
        // We check if we reached the maxProducts
        if (products.size() >= maxProducts) {
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
