package es.upm.etsisi.poo.Models.Ticket;

public class ProductEntry extends TicketEntry<Product> {
    public int amount;

    @Override
    public String toString() {
        return "{class:Product, id:"
                + product.getId()
                + ", name:'"
                + product.getName()
                + "', category:"
                + product.getCategory()
                + product.getPrice() + "}";
        if (!personalizations.isEmpty())
            sb.append(", personalizations:").append(this.personalizations);
    }

        /**
     * Method to set the personalization of the product
     * @param personalizations the personalizations to copy
     * @throws InvalidProductException controls that the personalizations are valid in the product
     */
    public void setPersonalizations(ArrayList<String> personalizations) throws InvalidProductException {
        if (personalizations.size() > this.maxPersonalization)
            throw new InvalidProductException
                    ("ERROR: The product can't have more than: "+this.maxPersonalization+" personalizations.");
        // if we can add the personalizations we copy them
        this.personalizations = personalizations;
//    }

    @Override
    public int getProductCount() {
        return amount;
    }

    @Override
    public double getPrice() {
        return product.getPrice() * amount;
    }

    @Override
    public boolean checkValidity() {
        return true;
    }
}
