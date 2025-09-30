package es.upm.etsisi.poo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private HashMap<Product, Integer> ticket;
    private HashMap<String, Integer> categories;

    public Ticket (Config config) {
        this.ticket = new HashMap<>();
        this.categories = new HashMap<>();

    }

    /**
     * Method to add products to ticket
     * @param product
     * @param amount
     * @return -1 if product is not added, 0 if product is added
     */
    public int addProduct (Product product, int amount){
        if (ticket.size() >= config.getNumMaxElementos()){
            // Product cannot be added due to override
            System.err.println("Too many products in ticket");
            return -1;
        }

        else {
            // Product can be added
            ticket.put(product, amount);
            System.out.println(ticket.toString());

            System.out.println("Ticket add: ok");

        }
        return -1;
    }

    public int deleteProdouct (int id){
        return -1;
    }
    public int updateProduct (Product product){
        return -1;
    }
    public double getPrice() {
        return -1;
    }


    public String toString() {
        String str = "";
        double totalPrice = 0;
        double totalDiscount = 0;

        for (Map.Entry<Product, Integer> entry : ticket.entrySet()) {
            Product producto = entry.getKey();
            Integer cantidad = entry.getValue();

            str += producto.toString();
            totalPrice += producto.getPrice() * cantidad;

            if (categories.get(producto.getCategory()) > 1) {
                double discount = producto.getPrice() - (producto.getPrice() * config.getDiscount(producto.getCategory()));
                str += "**discount -" + discount;
                totalDiscount += discount;
            }

            str += "\n";

            }

        str += "\nTotal price: " + totalPrice;
        str += "\nTotal discount: " + totalDiscount;
        str += "\nFinal price: " + (totalPrice - totalDiscount);

        return str;
    }
}
