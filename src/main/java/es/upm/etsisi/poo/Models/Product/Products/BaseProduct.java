package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.ProductType;

import java.io.Serializable;

public abstract class BaseProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final ProductType type;

    public BaseProduct(ProductType type) {
        this.type = type;
    }

    public BaseProduct(BaseProduct other) {
        this.type = other.type;
    }

    abstract public ProductID getID();
    public ProductType getType() { return type; }

    public boolean equals(Object other) {
        if (this == other) return true;
        else if (other.getClass() != this.getClass()) return false;
        BaseProduct product = (BaseProduct) other;
        return this.getID() == product.getID();
    }

    abstract public BaseProduct clone();
}