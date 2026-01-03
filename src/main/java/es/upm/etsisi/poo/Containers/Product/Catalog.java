package es.upm.etsisi.poo.Containers.Product;

import es.upm.etsisi.poo.Containers.Product.ProductTypes.BaseProduct;

import java.util.HashMap;
import java.util.Set;

/**
 * Class that implements a HashMap to store all the products available in the store
 */
public class Catalog {

    public Catalog() {
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
     *         -3 if the id already exists
     *         -2 if the product passed was a null;
     *         -1 if we already reached the maxProducts;
     */
    public int add(BaseProduct product) {
        // Discard null objects
        if (product == null) {
            return -2;
        }
        // We check if we reached the maxProducts
        if (products.size() >= MAX_PRODUCTS) {
            return -1;
        }
        // We check if the id is valid
        if (existsId(product.getId())) {
            return -3;
        }
        // Put the product in the map and print it
        products.put(product.getId(), product);
        return 0;
    }

    /**
     * Method to obtain a new id in ascendant order among the products in the catalog. When a product is removed and its
     * id was less than the current id, the next product added will have this id
     * @return new ID
     */
    public int getNewId() {
        Set<Integer> usedIds = products.keySet();
        int newId = 1;
        while (usedIds.contains(newId)) {
            newId++;
        }
        return newId;
    }

    /**
     * Method to delete a product from the catalog
     *
     * @param id to search the product
     * @return either the product that was removed or null if the product doesn't exist in the catalog.
     */
    public BaseProduct deleteProduct(int id) {
        BaseProduct product = this.getProduct(id);
        if (product != null) {
            // If the product exist in the catalog we delete it
            products.remove(id);
        }
        return product;
    }

    public boolean existsId(int id) {
        return this.getProduct(id) != null;
    }

    private final HashMap<Integer, BaseProduct> products;
    private final int MAX_PRODUCTS = 200;
}
