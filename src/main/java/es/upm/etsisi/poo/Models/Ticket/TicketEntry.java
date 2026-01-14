package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;

public abstract class TicketEntry<T extends GoodsProduct> {
    public void update(T newProduct) {
        product = newProduct.clone();
    };

    public abstract String toString();
    public abstract int getProductCount();
    public abstract double getPrice();

    public abstract boolean checkValidity();

    protected T product;
}