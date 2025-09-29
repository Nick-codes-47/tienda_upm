package es.upm.etsisi.poo;

/**
 * This class is used to create objects with the characteristics we need for our products
 */
public class Product {
    private Category category;
    private int id;
    private String name;
    private int price;

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public int getPrice() { return this.price; }
    public void setPrice(int price) { this.price = price; }
    public Category getCategory() { return this.category; }
    public void setCategory(Category category) { this.category = category; }

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
