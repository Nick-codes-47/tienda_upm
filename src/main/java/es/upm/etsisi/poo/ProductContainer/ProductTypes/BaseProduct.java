package es.upm.etsisi.poo.ProductContainer.ProductTypes;

public class BaseProduct {

    public BaseProduct(int id, String name, double price) throws InvalidProductException {
        // id and price must be positive numbers
        if (id <= 0 || price <= 0) {
            StringBuilder msg = new StringBuilder("ERROR: Product ");
            if (id <= 0) msg.append("ID");
            if (id <= 0 && price <= 0) msg.append(" and ");
            if (price <= 0) msg.append("price");
            msg.append(" must be positive and higher than 0");
            throw new InvalidProductException(msg.toString());
        }
        else if (name == null || name.isEmpty() || name.length() >= 100) {
            throw new InvalidProductException("ERROR: Product's name is invalid (must have less than 100 characters)");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static class InvalidProductException extends Exception {
        public InvalidProductException(String message) {
            super(message);
        }
    }


    public int getId() { return id; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name;}
    public double getPrice() { return this.price; }

    private final int id;
    private String name;
    private double price;
}
