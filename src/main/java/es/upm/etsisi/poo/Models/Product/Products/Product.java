package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductName;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.Category;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.ProductType;
import es.upm.etsisi.poo.AppExceptions.InvalidProductException;

public class Product extends GoodsProduct {

    private static final long serialVersionUID = 1L;

    public Product(ProductID ID, ProductName name, String category, double price) throws InvalidProductException {
        super(ProductType.PRODUCT, ID, name, price);

        try {
            this.category = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidProductException("category is invalid");
        }

        this.maxPersonalization = 0;
    }

    public Product(ProductID ID, ProductName name, String category, double price, int numPersonalizations) throws InvalidProductException {
        super(ProductType.CUSTOM, ID, name, price);

        try {
            this.category = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidProductException("category is invalid");
        }

        this.maxPersonalization = numPersonalizations;
    }

    public Product(Product other) {
        super(other);
        this.category = other.category;
        this.maxPersonalization = other.maxPersonalization;
    }

    public Category getCategory() { return this.category; }
    public int getMaxPersonalization() { return this.maxPersonalization; }

    @Override
    public Product clone() {
        return new Product(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{class:").append(super.getType())
                .append(", id:").append(super.getID())
                .append(", name:'").append(super.getName())
                .append("', category:").append(category)
                .append(", price:").append(super.getPrice());
        if (maxPersonalization != 0)
            sb.append(", maxPersonal:").append(this.maxPersonalization);
        sb.append("}");

        return sb.toString();
    }

    private final Category category;
    private final int maxPersonalization;
}