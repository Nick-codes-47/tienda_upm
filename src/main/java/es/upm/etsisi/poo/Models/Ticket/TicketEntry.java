package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Core.AppException;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;

import java.io.Serializable;

public abstract class TicketEntry<ProductType extends BaseProduct> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected TicketEntry(ProductType product) {
        this.product = product;
    }

    public void update(ProductType newProduct) throws AppException {
        if (newProduct.getID() == product.getID())
            product = (ProductType) newProduct.clone();
        else throw new AppException("");
    };

    public abstract String toString();
    public abstract int getProductCount();
    public abstract double getPrice();

    public abstract boolean checkValidity();

    protected ProductType product;
}