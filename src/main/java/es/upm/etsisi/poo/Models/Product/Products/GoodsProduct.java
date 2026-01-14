package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Models.Core.AppException;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductName;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.ProductType;

/**
 * This class is used to create objects with the characteristics we need for our products
 */
public abstract class GoodsProduct extends BaseProduct {

    public GoodsProduct(ProductType type, ProductID ID, ProductName name, double price) throws InvalidProductException {
        super(type);

        this.ID = ID;
        this.name = name;

        if (price <= 0) {
            throw new InvalidProductException("price must be positive and higher than 0");
        }

        this.price = price;
    }

    public GoodsProduct(GoodsProduct other) {
        super(other);

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

    private final ProductID ID;
    private ProductName name;
    private final double price;
}
