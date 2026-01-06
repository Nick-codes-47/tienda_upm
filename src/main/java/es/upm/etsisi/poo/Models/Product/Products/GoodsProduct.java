package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Exceptions.InvalidProductException;

public class GoodsProduct extends BaseProduct {

    public GoodsProduct(int id, String name, double price) throws InvalidProductException {
        super(id);
        // id and price must be positive numbers
        if (price <= 0) {
            throw new InvalidProductException("ERROR: Product price must be positive and higher than 0");
        }
        else if (name == null || name.isEmpty() || name.length() >= 100) {
            throw new InvalidProductException("ERROR: Product's name is invalid (must have less than 100 characters)");
        }
        this.name = name;
        this.price = price;
    }

    //public static class InvalidProductException extends Exception {
    //    public InvalidProductException(String message) {
    //        super(message);
    //    }
    //}


    public String getName() { return this.name; }
    public void setName(String name) { this.name = name;}
    public double getPrice() { return this.price; }

    private String name;
    private double price;
}
