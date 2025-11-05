package es.upm.etsisi.poo.ProductContainer;

public class CustomProduct extends Product {

    public CustomProduct(int id, String name, double price, String category, int pers) throws InvalidProductException {
        super(id,name,category,price);
        this.personalizable = pers;
    }

    public int getMaxPersonalizable() {
        return personalizable;
    }

    private int personalizable;
}
