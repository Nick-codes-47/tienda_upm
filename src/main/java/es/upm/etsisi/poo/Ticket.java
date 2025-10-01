package es.upm.etsisi.poo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private HashMap<Product, Integer> ticket;
    private HashMap<String, Integer> categories;
    private int numMaxElements;

    public Ticket (Config config) {
        this.ticket = new HashMap<>();
        this.categories = new HashMap<>();
        numMaxElements = config.getNumMaxElementos();
    }

    /**
     * Method to add products to ticket
     * @param product
     * @param quantity
     * @return
     *      Return -1 if the number of products in the ticket is already maximum products
     *      Return -2 if it’s not maximum products yet but the quantity I want to add exceeds maximum products
     *      Return 0 if product can be added
     */
    public int addProduct (Product product, int quantity){
        if (ticket.size() >= numMaxElements){
            // Product cannot be added due to maximum products already
            return -1;
        }
        else if (ticket.size() + quantity > numMaxElements){
            return -2;
        }
        else {
            // Product can be added
            ticket.put(product, quantity);
            System.out.println(ticket.toString());
            return 0;
        }
    }

    /**
     * Removes a product from the ticket by its id.
     * @param id The id of the product to remove.
     * @return
     *      0 if the product was found and removed successfully,
     *     -1 if no product with the given id exists in the ticket.
     */
    public int deleteProduct(int id) {
        Product productToRemove = null;
        for (Product p : ticket.keySet()) {
            if (p.getId() == id) {
                productToRemove = p;
                break;
            }
        }

        if (productToRemove != null) {
            ticket.remove(productToRemove);
            return 0; // found
        }
        return -1; // not found
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

            if (categories.get(producto.getCategory().toString()) > 1) {
                double discount = producto.getPrice() - (producto.getPrice() * config.getDiscount(producto.getCategory().toString()));
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
