package es.upm.etsisi.poo.Models.Product.Products.Core;

import es.upm.etsisi.poo.Models.Core.AppException;

public class ProductName {

    public ProductName(String name) throws InvalidCatalogNameException {
        if (name == null)
            throw new InvalidCatalogNameException("null");
        else if (name.isEmpty())
            throw new InvalidCatalogNameException("empty");
        else if (name.length() >= MAX_CHARACTER_NAME_LENGTH)
            throw new InvalidCatalogNameException("more than 100 characters");

        this.name = name;
    }

    public ProductName(ProductName other) {
        this.name = other.name;
    }

    public String getName() { return name; }

    public static class InvalidCatalogNameException extends AppException {
        public InvalidCatalogNameException(String message) {
            super("Catalog item name can not be " + message);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof ProductName otherName)) return false;
        return name.equals(otherName.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    private final static int MAX_CHARACTER_NAME_LENGTH = 100;

    private final String name;
}
