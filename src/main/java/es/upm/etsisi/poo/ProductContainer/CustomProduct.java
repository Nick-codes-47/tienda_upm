package es.upm.etsisi.poo.ProductContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CustomProduct extends Product {

    public CustomProduct(int id, String name, String category, double price, int pers) throws InvalidProductException {
        super(id,name,category,price);
        if (pers < 1)
            throw new InvalidProductException("ERROR: A CustomProduct must have at leat one personalizable.");
        this.maxPersonalization = pers;
    }

    /**
     * Method to set the personalization of the product
     * @param personalizations the personalizations to copy
     * @throws InvalidProductException controls that the personalizations are valid in the product
     */
    public void setPersonalizations(String[] personalizations) throws InvalidProductException {
        if (personalizations.length > this.maxPersonalization)
            throw new InvalidProductException
                    ("ERROR: The product can't have more than: "+this.maxPersonalization+" personalizations.");
        // if we can add the personalizations we copy them
        Collections.addAll(this.personalizations, personalizations);
    }

    /**
     * Compares this product to another with the ID
     * @param obj (another)
     * @return whether the IDs match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomProduct other = (CustomProduct) obj;
        return super.getId() == other.getId();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{class:ProductPersonalized")
                .append(", id:")
                .append(super.getId())
                .append(", name:'")
                .append(super.getName())
                .append("', category:")
                .append(super.getCategory())
                .append(", price:")
                .append(super.getPrice())
                .append(", maxPersonal:")
                .append(this.maxPersonalization);
        if (!personalizations.isEmpty())
            sb.append(", personalizations:").append(this.personalizations);
        sb.append("}");

        return sb.toString();
    }


    private final int maxPersonalization;
    private final ArrayList<String> personalizations = new ArrayList<>();
}
