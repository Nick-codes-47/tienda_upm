package es.upm.etsisi.poo.Models.Product;

import es.upm.etsisi.poo.Models.Core.AppException;
import es.upm.etsisi.poo.Models.Core.AppID;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.Core.ServiceID;
import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Product.Products.ServiceProduct;

import java.util.HashMap;
import java.util.Set;

/**
 * Class that implements a HashMap to store all the products available in the store
 */
public class Catalog {

    public Catalog() {
        products = new HashMap<>();
        services = new HashMap<>();
    }

    /**
     * Method to search a product in the catalog
     *
     * @param ID id of the product to search it
     * @return null if the id is not correct.
     * the product with the id if it's correct
     */
    public GoodsProduct get(ProductID ID) {
        return products.get(ID);
    }

    public ServiceProduct get(ServiceID ID) {
        return services.get(ID);
    }

    public HashMap<ProductID, GoodsProduct> getProducts() {
        return products;
    }

    public HashMap<ServiceID, ServiceProduct> getServices() {
        return services;
    }

    public HashMap<AppID, BaseProduct> getAll() {
        HashMap<AppID, BaseProduct> all = new HashMap<>();
        all.putAll(this.products);
        all.putAll(this.services);
        return all;
    }

    /**
     * Method to add a product to the catalog so it is available to buy it
     *
     * @param product the product that needs to be added to the catalog
     * @return 0 if the product was added to the catalog without problem;
     *         -3 if the id already exists
     *         -2 if the product passed was a null;
     *         -1 if we already reached the maxProducts;
     */
    public int add(GoodsProduct product) {
        // Discard null objects
        if (product == null) {
            return -2;
        }
        // We check if we reached the maxProducts
        if (products.size() + services.size() >= MAX_PRODUCTS) {
            return -1;
        }

        // We check if the id is valid
        if (get(product.getID()) != null) {
            return -3;
        }
        // Put the product in the map and print it
        products.put(product.getID(), product);
        return 0;
    }

    public int add(ServiceProduct service) {
        // Discard null objects
        if (service == null) {
            return -2;
        }
        // We check if we reached the maxProducts
        if (products.size() + services.size() >= MAX_PRODUCTS) {
            return -1;
        }
        // We check if the id is valid
        if (get(service.getID()) != null) {
            return -3;
        }
        // Put the product in the map and print it
        services.put(service.getID(), service);
        return 0;
    }

    /**
     * Method to obtain a new id in ascendant order among the products in the catalog. When a product is removed and its
     * id was less than the current id, the next product added will have this id
     * @return new ID
     */
    public ProductID getNewProductID() throws AppException {
        Set<ProductID> usedIDs = products.keySet();
        int newID = 1;
        while (usedIDs.contains(new ProductID(newID))) {
            newID++;
        }
        return new ProductID(newID);
    }

    public ServiceID getNewServiceID() throws AppException {
        Set<ServiceID> usedIDs = services.keySet();
        int newID = 1;
        while (usedIDs.contains(new ServiceID(newID))) {
            newID++;
        }
        return new ServiceID(newID);
    }

    /**
     * Method to delete a product from the catalog
     *
     * @param ID to search the product
     * @return either the product that was removed or null if the product doesn't exist in the catalog.
     */
    public GoodsProduct delete(ProductID ID) {
        GoodsProduct product = this.get(ID);
        if (product != null) {
            // If the product exist in the catalog we delete it
            products.remove(ID);
        }
        return product;
    }

    public ServiceProduct delete(ServiceID ID) {
        ServiceProduct product = this.get(ID);
        if (product != null) {
            // If the product exist in the catalog we delete it
            services.remove(ID);
        }
        return product;
    }

    private final HashMap<ProductID, GoodsProduct> products;
    private final HashMap<ServiceID, ServiceProduct> services;
    private final int MAX_PRODUCTS = 200;
}
