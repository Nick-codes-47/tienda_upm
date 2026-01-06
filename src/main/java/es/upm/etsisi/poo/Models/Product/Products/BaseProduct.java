package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Exceptions.InvalidProductException;

public class BaseProduct {
    private final int id;

    public BaseProduct(int id) throws InvalidProductException {
        if (id < 0)
            throw new InvalidProductException("ERROR: Product ID must be positive and higher than 0.");
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
