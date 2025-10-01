package es.upm.etsisi.poo;

public class Config {
    public boolean validCategory(String category){
        return category.equalsIgnoreCase("true");
    }

    public double getDiscount(String category) {
        return 0;
    }

    public int getMaxProductsPerTicket() { return 200; }
}
