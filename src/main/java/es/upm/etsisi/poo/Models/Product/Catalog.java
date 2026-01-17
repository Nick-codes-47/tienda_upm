package es.upm.etsisi.poo.Models.Product;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.Core.ServiceID;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that implements a HashMap to store all the products available in the store
 */
public class Catalog implements Serializable {

    private static final long serialVersionUID = 1L;

    public Catalog() {
        products = new HashMap<>();
    }

    public void loadData(HashMap<ProductID, BaseProduct> loadedProducts) {
        if (loadedProducts != null) {
            this.products.clear();
            this.products.putAll(loadedProducts);
        }
    }

    /**
     * Method to search a product in the catalog
     *
     * @param ID id of the product to search it
     * @return null if the id is not correct.
     * the product with the id if it's correct
     */
    public BaseProduct get(ProductID ID) {
        return products.get(ID);
    }

    public HashMap<ProductID, BaseProduct> getProducts() {
        return products;
    }

    /**
     * Method to add a product to the catalog so it is available to buy it
     *
     * @param product the product that needs to be added to the catalog
     * @return 0 if the product was added to the catalog without problem;
     */
    public int add(BaseProduct product)
            throws NullAppEntityException, FullContainerException, EntityAlreadyExistsException {
        if (product == null) throw new NullAppEntityException("product");

        if (products.size() >= MAX_PRODUCTS) throw new FullContainerException();

        if (get(product.getID()) != null) throw new EntityAlreadyExistsException("product", product.getID().toString());

        products.put(product.getID(), product);
        return 0;
    }

    /**
     * Method to obtain a new id in ascendant order among the products in the catalog. When a product is removed and its
     * id was less than the current id, the next product added will have this id
     * @return new ID
     */
    public ProductID getNewProductID() throws AppException {
        Set<ProductID> usedIDs = products.keySet().stream()
                .filter(id -> id.getClass() == ProductID.class)
                .collect(Collectors.toSet());

        int countID = 1;
        ProductID newID = new ProductID(countID);
        while (usedIDs.contains(newID)) {
            newID = new ProductID(++countID);
        }

        return newID;
    }

    public ServiceID getNewServiceID() throws AppException {
        Set<ProductID> usedIDs = products.keySet().stream()
                .filter(ServiceID.class::isInstance)
                .collect(Collectors.toSet());

        int countID = 1;
        ServiceID newID = new ServiceID(countID);
        while (usedIDs.contains(newID)) {
            newID = new ServiceID(++countID);
        }

        return newID;
    }

    /**
     * Method to delete a product from the catalog
     *
     * @param ID to search the product
     * @return either the product that was removed or null if the product doesn't exist in the catalog.
     */
    public BaseProduct delete(ProductID ID) throws AppEntityNotFoundException {
        BaseProduct product = this.get(ID);
        if (product == null) throw new AppEntityNotFoundException("product", ID.toString());

        products.remove(ID);

        return product;
    }

    private final HashMap<ProductID, BaseProduct> products;
    private final int MAX_PRODUCTS = 200;
}