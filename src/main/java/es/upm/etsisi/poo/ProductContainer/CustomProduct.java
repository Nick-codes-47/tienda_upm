package es.upm.etsisi.poo.ProductContainer;

public class CustomProduct extends Product {

    public CustomProduct(int id, String name, String category, double price, int pers) throws InvalidProductException {
        super(id,name,category,price);
        if (pers < 1)
            throw new InvalidProductException("ERROR: A CustomProduct must have at leat one personalizable.");
        this.personalizable = pers;
    }

    public int getMaxPersonalizable() {
        return personalizable;
    }

    private int personalizable;
}
