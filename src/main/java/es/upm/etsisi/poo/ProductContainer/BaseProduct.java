package es.upm.etsisi.poo.ProductContainer;

abstract class BaseProduct {
    private final String name;
    private final double price;

    public BaseProduct(String name, double price) {
        // TODO HANDLE REQUIREMENTS
        if (price <= 0) {
            System.out.println("ERROR: Product price must be positive and higher than 0");
        }
        if (name == null || name.isEmpty() || name.length() >= 100) {
            System.out.println("ERROR: Product's name is invalid (must have less than 100 characters)");
        }
        this.name = name;
        this.price = price;
    }

    public String getName() { return this.name; }
    public double getPrice() { return this.price; }
}
