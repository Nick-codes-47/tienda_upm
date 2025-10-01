package es.upm.etsisi.poo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private Config config;
    private HashMap<Product, Integer> ticket;
    private HashMap<String, Integer> categories;
    private int numMaxElements;

    public Ticket (Config config) {
        this.config = config;
        this.ticket = new HashMap<>();
        this.categories = new HashMap<>();
        //this.numMaxElements = config.getNumMaxElementos();
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
            System.out.println(ticket);
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

    /**
     * Updates the details of a product in the ticket while keeping its existing quantity.
     *
     * @param updatedProduct The product with updated information.
     * @return
     *      0 if the product was found and updated successfully,
     *     -1 if the product does not exist in the ticket.
     */
    public int updateProduct(Product updatedProduct) {
        Product existingProduct = null;

        // Search product by its id
        for (Product p : ticket.keySet()) {
            if (p.getId() == updatedProduct.getId()) {
                existingProduct = p;
                break;
            }
        }

        if (existingProduct != null) {
            // Keep the quantity
            int quantity = ticket.get(existingProduct);

            // Remove the old product and add the new one with the same quantity of it
            ticket.remove(existingProduct);
            ticket.put(updatedProduct, quantity);
            return 0; // updated successfully
        }

        return -1; // not found
    }


    /**
     * Clears the current ticket and creates a new empty one.
     */
    public void resetTicket() {
        this.ticket = new HashMap<>();
        this.categories = new HashMap<>();
    }


    /**
     * Builds a string representation of the ticket.
     * <p>
     * The string includes the list of products with their details,
     * applied discounts by category when applicable,
     * and a summary with total price, total discount, and final price.
     *
     * @return A formatted string representation of the ticket and its summary.
     */
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
