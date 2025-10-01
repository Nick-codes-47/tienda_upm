package es.upm.etsisi.poo;

/**
 * This class is used to create objects with the characteristics we need for our products
 */
public class Product {
    private String category;
    private int id;
    private String name;
    private int price;

    /**
     * This constructor is done to introduce all the attributes the user wants for the product
     * @param category The category of the product
     * @param id The id of the product
     * @param name The name of the product
     * @param price The price of the product
     */
    public Product(String category, int id, String name, int price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public int getPrice() { return this.price; }
    public void setPrice(int price) { this.price = price; }
    public String getCategory() { return this.category; }
    public void setCategory(String category) { this.category = category; }

    /**
     * Compares this product to another with the ID
     * @param other (another)
     * @return whether the IDs match
     */
    public boolean equals(Product other) { return this.id == other.id; }

    @Override
    public String toString() {
        return "{class:Product, id:"+this.id+", name:'"+this.name+"', category:"+this.category+", price:"+this.price+"}";
    }
}
