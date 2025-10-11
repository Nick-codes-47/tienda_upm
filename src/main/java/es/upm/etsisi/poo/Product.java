package es.upm.etsisi.poo;

/**
 * This class is used to create objects with the characteristics we need for our products
 */
public class Product {
    private String category;
    private int id;
    private String name;
    private double price;

    /**
     * This constructor is done to introduce all the attributes the user wants for the product
     * @param category The category of the product
     * @param id The id of the product
     * @param name The name of the product
     * @param price The price of the product
     */
    public Product(String category, int id, String name, double price) throws InvalidProductException {
        // id and price must be positive numbers
        if (id <= 0 || price <= 0) {
            StringBuilder msg = new StringBuilder("ERROR: Product ");
            if (id <= 0) msg.append("ID");
            if (id <= 0 && price <= 0) msg.append(" and ");
            if (price <= 0) msg.append("price");
            msg.append(" must be positive and higher than 0");
            throw new InvalidProductException(msg.toString());
        }
        if (name == null || name.isEmpty() || name.length() > 100) {
            throw new InvalidProductException("ERROR: Product's name is invalid (must have less than 100 characters)");
        }
        this.category = category;
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return this.price; }
    public void setPrice(double price) { this.price = price; }
    public String getCategory() { return this.category; }
    public void setCategory(String category) { this.category = category; }

    /**
     * Compares this product to another with the ID
     * @param obj (another)
     * @return whether the IDs match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product other = (Product) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }


    @Override
    public String toString() {
        return "{class:Product, id:"+this.id+", name:'"+this.name+"', category:"+this.category.toUpperCase()+
                ", price:"+this.price+"}";
    }

    public static class InvalidProductException extends Exception {
        public InvalidProductException(String message) {
            super(message);
        }
    }
}
