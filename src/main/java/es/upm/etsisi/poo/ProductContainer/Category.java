package es.upm.etsisi.poo.ProductContainer;

public enum Category {
    MERCH(0), STATIONERY(5), CLOTHES(7), BOOK(10), ELECTRONICS(3);

    private final double discount;

    Category(double discount) {
        this.discount = discount;
    }
    public double getDiscount() { return this.discount; }
}
