package es.upm.etsisi.poo;

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
     * @param product (another)
     * @return whether the IDs match
     */
    public boolean equals(Product product) { return this.id == product.id; }

}
