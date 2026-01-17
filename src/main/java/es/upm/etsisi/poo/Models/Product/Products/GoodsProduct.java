package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Core.ProductName;
import es.upm.etsisi.poo.AppExceptions.InvalidProductException;

/**
 * This class is used to create objects with the characteristics we need for our products
 */
public abstract class GoodsProduct<T extends GoodsProduct<T>> extends BaseProduct<T> {

    private static final long serialVersionUID = 1L;

    private final ProductID ID;
    private ProductName name;
    private final double price;

    public GoodsProduct(ProductID ID, ProductName name, double price) throws InvalidProductException {
        this.ID = ID;
        this.name = name;

        if (price <= 0) {
            throw new InvalidProductException("price must be positive and higher than 0");
        }

        this.price = price;
    }

    public GoodsProduct(GoodsProduct<T> other) {
        this.ID = other.ID;
        this.name = other.name;
        this.price = other.price;
    }

    @Override
    public ProductID getID() { return ID; }
    public double getPrice() { return this.price; }
    public ProductName getName() { return name; }
    public void setName(ProductName name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}