package es.upm.etsisi.poo.Models.Ticket.Core;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;

import java.io.Serializable;

public abstract class TicketEntry<ProductType extends BaseProduct> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected ProductType product;

    protected TicketEntry(ProductType product) {
        this.product = product;
    }

    public void update(ProductType newProduct) throws AppException {
        if (newProduct.getID() == product.getID())
            product = (ProductType) newProduct.clone();
        else throw new AppEntityNotFoundException(newProduct.getType().toString(), newProduct.getID().toString());
    };

    public ProductType getProduct() { return product; };
    public abstract int getProductCount();
    public abstract double getPrice();

    public abstract boolean checkValidity();

    public abstract String toString();
}