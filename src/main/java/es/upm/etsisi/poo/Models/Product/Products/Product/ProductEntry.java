package es.upm.etsisi.poo.Models.Product.Products.Product;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;

import java.util.ArrayList;
import java.util.List;

public class ProductEntry extends TicketEntry<Product, ProductEntry> {

    private static final long serialVersionUID = 1L;

    public int amount;
    private List<String> personalizations;

    public ProductEntry(Product product) {
        super(product);
    }

    @Override
    public String toString() {
        String print = "{class:" + product.getType()
                + ", id:" + product.getID()
                + ", name:'" + product.getName() + "'"
                + ", category:" + product.getCategory()
                + ", price:" + product.getPrice();
        if (personalizations != null && !personalizations.isEmpty())
            print += (", personalizations:") + this.personalizations;

        print += "}";

        return print;
    }

    /**
     * Method to set the personalization of the product
     * @param personalizations the personalizations to copy
     */
    public void setPersonalizations(ArrayList<String> personalizations) throws PersonalizationExceedsMaxException {
        if (personalizations.size() > product.getMaxPersonalization())
            throw new PersonalizationExceedsMaxException();
        // if we can add the personalizations we copy them
        this.personalizations = personalizations;
    }

    @Override
    public int getProductCount() {
        return amount;
    }

    @Override
    public double getPrice() {
        return product.getPrice() * amount;
    }

    @Override
    public void accumulate(ProductEntry more) {
        if (more.product.getID() == this.product.getID())
            this.amount += more.amount;
    }

    public class PersonalizationExceedsMaxException extends AppException {
        public PersonalizationExceedsMaxException() {
            super("The product can't have more than: "+ product.getMaxPersonalization() + " personalizations.");
        }
    }
}