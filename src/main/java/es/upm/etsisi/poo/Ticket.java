package es.upm.etsisi.poo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private Config config;
    private HashMap<Product, Integer> ticket;
    private HashMap<String, Integer> categories;
    private int numMaxElements;

    public Ticket(Config config) {
        this.config = config;
        this.ticket = new HashMap<>();
        this.categories = config.getCategories();
        this.numMaxElements = config.getMaxProductPerTicket();
    }

    /**
     * Method to add products to ticket
     *
     * @param product
     * @param quantity
     * @return Return -1 if the number of products in the ticket is already maximum products
     * Return -2 if it’s not maximum products yet but the quantity I want to add exceeds maximum products
     * Return 0 if product can be added
     */
    public int addProduct(Product product, int quantity) {
        if (ticket.size() >= numMaxElements) return -1;
        else if ((ticket.size() + quantity) > numMaxElements) return -2;
        else {
            ticket.put(product, quantity);
            System.out.println(ticket);
            return 0;
        }
    }

    /**
     * Removes a product from the ticket by its id.
     *
     * @param productToDelete The product to remove.
     * @return 0 if the product was found and removed successfully,
     * -1 if the product does not exist in the ticket.
     */
    public int deleteProduct(Product productToDelete) {
        if (ticket.containsKey(productToDelete)) {
            ticket.remove(productToDelete);
            return 0; // Product deleted
        }
        return -1; // Product not found
    }


    /**
     * Updates the details of a product in the ticket while keeping its existing quantity.
     *
     * @param updatedProduct The product with updated information.
     * @return 0 if the product was found and updated successfully,
     * -1 if the product does not exist in the ticket.
     */
    public int updateProduct(Product updatedProduct) {
        if (ticket.containsKey(updatedProduct)) {
            int quantity = ticket.get(updatedProduct);
            ticket.put(updatedProduct, quantity);
            return 0;
        }
        return -1;
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        double totalPrice = 0;
        double totalDiscount = 0;

        for (Map.Entry<Product, Integer> entry : ticket.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            String category = product.getCategory().toString();

            sb.append(product);
            totalPrice += product.getPrice() * quantity;

            if (categories.get(category) > 1) {
                double discount = product.getPrice() - (product.getPrice() * config.getDiscount(category));
                sb.append(" **discount -").append(discount);
                totalDiscount += discount;
            }
            sb.append("\n");
        }

        sb.append("\nTotal price: ").append(totalPrice);
        sb.append("\nTotal discount: ").append(totalDiscount);
        sb.append("\nFinal price: ").append(totalPrice - totalDiscount);

        return sb.toString();
    }

}
