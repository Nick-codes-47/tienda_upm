package es.upm.etsisi.poo.Models.Product.Products.Core;

import es.upm.etsisi.poo.Models.Core.AppException;
import es.upm.etsisi.poo.Models.Core.AppID;

import java.util.Objects;

public class ProductID implements AppID {

    public ProductID(int ID) throws InvalidProductIDException {
        if (ID <= 0)
            throw new InvalidProductIDException("0 or less than 0");

        this.baseID = ID;
    }

    public ProductID(ProductID other) {
        baseID = other.baseID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductID other = (ProductID) obj;
        return baseID == other.baseID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), baseID);
    }

    @Override
    public String toString() {
        return Integer.toString(baseID);
    }

    public static class InvalidProductIDException extends AppException {
        public InvalidProductIDException(String message) {
            super("Product ID can not be " + message);
        }
    }

    protected final int baseID;
}
