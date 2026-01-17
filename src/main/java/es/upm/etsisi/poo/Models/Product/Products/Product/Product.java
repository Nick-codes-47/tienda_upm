package es.upm.etsisi.poo.Models.Product.Products.Product;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Core.Copyable;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Core.ProductName;
import es.upm.etsisi.poo.Models.Product.ProductEnums.Category;
import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.AppExceptions.InvalidCategoryException;
import es.upm.etsisi.poo.AppExceptions.NonPositiveNumberException;

import java.util.ArrayList;

public class Product extends GoodsProduct<Product> implements Copyable<Product> {

    private static final long serialVersionUID = 1L;

    private Category category;
    private final int maxPersonalization;

    public Product(ProductID ID, ProductName name, String category, double price)
            throws NonPositiveNumberException, InvalidCategoryException {
        super(ID, name, price);

        initializeCategory(category);

        this.maxPersonalization = 0;
    }

    public Product(ProductID ID, ProductName name, String category, double price, int numPersonalizations)
            throws InvalidCategoryException, NonPositiveNumberException {
        super(ID, name, price);

        initializeCategory(category);

        if  (numPersonalizations <= 0) throw new NonPositiveNumberException("Personalizations");

        this.maxPersonalization = numPersonalizations;
    }

    private void initializeCategory(String category) throws InvalidCategoryException {
        try {
            this.category = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryException(category);
        }
    }

    public Product(Product other) {
        super(other);
        this.category = other.category;
        this.maxPersonalization = other.maxPersonalization;
    }

    public String getType() { return (maxPersonalization > 0)? "ProductPersonalizable" :  "Product"; }
    public Category getCategory() { return this.category; }
    public int getMaxPersonalization() { return this.maxPersonalization; }

    @Override
    public ProductEntry toTicketEntry(String[] rawArgs) throws AppException {
        EntryArgs args = new EntryArgs(rawArgs);
        ProductEntry entry = new ProductEntry(this);

        entry.amount = args.amount;
        if (args.personalizations != null)
            entry.setPersonalizations(args.personalizations);
        return entry;
    }

    @Override
    public Product copy() {
        return new Product(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{class:").append(getType())
                .append(", id:").append(super.getID())
                .append(", name:'").append(super.getName())
                .append("', category:").append(category)
                .append(", price:").append(super.getPrice());
        if (maxPersonalization != 0)
            sb.append(", maxPersonal:").append(this.maxPersonalization);
        sb.append("}");

        return sb.toString();
    }

    private class EntryArgs {
        public int amount;
        public ArrayList<String> personalizations = null;

        public EntryArgs(String[] args) {

        }
    }
}