package es.upm.etsisi.poo;

import java.util.ArrayList;

public class Ticket {
    ArrayList<Product> ticket;
    int numMaxElementos;

    Ticket (Config config) {
        this.ticket = new ArrayList<>();
        this.numMaxElementos = config.getNumMaxElementos();
    }

    public int addProduct (Product product, int amount){
        if (ticket.size() >= 100){
            System.out.println("Too many products");
            return -1;
        }

        else {
            // El producto puede introducirse
            ticket.add(product);
            System.out.println("{Class: Product, id: " + product.getId() +
                    ", name:'" + product.getName() +
                    "', category: " + product.getCategory() +
                    ", price:" + product.getPrice() + "}");

        }
        return -1;
    }
    public int deleteProduct(int id){
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
        for (int i = 0; i < ticket.size(); i++) {
            str += ticket.get(i).getName() + ": " + ticket.get(i).getPrize() + "€\n";
        }
        return str;
    }

    public void resetTicket() {
        this.ticket =  new ArrayList<>();
        this.numMaxElementos = 200;
    }
}
