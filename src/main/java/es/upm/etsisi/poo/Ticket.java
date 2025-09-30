package es.upm.etsisi.poo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticket {
    HashMap<Product, Integer> ticket;

    Ticket (Config config) {
        this.ticket = new HashMap<>();
    }

    public int addProduct (Product product, int amount){
        if (ticket.size() >= config.getNumMaxElementos()){ // Ticket supera el limite de elementos
            System.err.println("Too many products in ticket");
            return -1;
        }

        else { // El producto puede introducirse
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
        double finalPrice = 0;

        for (Map.Entry<Product, Integer> entry : ticket.entrySet()) {
            Product producto = entry.getKey();
            Integer cantidad = entry.getValue();
            str += producto.toString();
            totalPrice += producto.getPrice() * cantidad;

            if (cantidad > 1){
                str += "**discount";
                totalDiscount += cantidad * config.getDiscount(producto.getCategory());
            }
        }
        str += "\nTotal price: " + totalPrice;
        str += "\nTotal discount: " + totalDiscount;
        str += "\nFinal price: " + finalPrice;

        return str;
    }
}
