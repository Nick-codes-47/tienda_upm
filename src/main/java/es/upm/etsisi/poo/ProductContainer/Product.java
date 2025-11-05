package es.upm.etsisi.poo.ProductContainer;

/**
 * This class is used to create objects with the characteristics we need for our products
 */
public class Product extends BaseProduct {

    /**
     * This constructor is done to introduce all the attributes the user wants for the product
     * @param id to recognize the product among others
     * @param category The category of the product
     * @param name The name of the product
     * @param price The price of the product
     */
    public Product(int id, String name, String category, double price) throws InvalidProductException {
        super(id,name,price);
        this.category = category;
    }

    public int getId() { return super.getId(); }
    public String getName() { return super.getName() ; }
    public void setName(String name) { super.setName(name); }
    public double getPrice() { return super.getPrice(); }
    public void setPrice(double price) { super.setPrice(price); }
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
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.getId());
    }


    @Override
    public String toString() {
        return "{class:Product, id:"+this.getId()+", name:'"+this.getName()+"', category:"+this.category.toUpperCase()+
                ", price:"+this.getPrice()+"}";
    }


    private String category;
}
