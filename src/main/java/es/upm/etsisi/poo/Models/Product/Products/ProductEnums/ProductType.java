package es.upm.etsisi.poo.Models.Product.Products.ProductEnums;

public enum ProductType {
    PRODUCT("Product"),
    EVENT("EventProduct"),
    SERVICE("ServiceProduct"),
    CUSTOM("ProductPersonalized");

    private final String print;

    ProductType(String print) {
        this.print = print;
    }

    @Override
    public String toString() {
        return print;
    }
}
