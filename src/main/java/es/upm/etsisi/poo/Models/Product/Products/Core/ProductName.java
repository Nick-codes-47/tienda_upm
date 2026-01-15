package es.upm.etsisi.poo.Models.Product.Products.Core;

import es.upm.etsisi.poo.Models.Core.AppException;

public class ProductName {

    public ProductName(String name) throws InvalidCatalogNameException {
        if (name == null)
            throw new InvalidCatalogNameException("be null");
        else if (name.isEmpty())
            throw new InvalidCatalogNameException("be empty");
        else if (name.length() >= MAX_CHARACTER_NAME_LENGTH)
            throw new InvalidCatalogNameException("be more than 100 characters");
        else if ("0123456789".contains("" + name.charAt(0)))
            throw new InvalidCatalogNameException("start with a number");

        this.name = name;
    }

    public ProductName(ProductName other) {
        this.name = other.name;
    }

    public String getName() { return name; }

    public static class InvalidCatalogNameException extends AppException {
        public InvalidCatalogNameException(String message) {
            super("Catalog item name can not " + message);
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
