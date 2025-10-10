package es.upm.etsisi.poo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private App app;
    private HashMap<Product, Integer> ticket;
    private HashMap<String, Integer> categories;
    private int numMaxElements;

    public Ticket (App app) {
        this.app = app;
        this.ticket = new HashMap<>();
        this.categories = new HashMap<>();
        this.numMaxElements = app.config.getMaxProductPerTicket();

        for (String category : app.config.getCategories())
        {
            categories.put(category, 0);
        }
    }

    /**
     * Handles a user request directed to the Ticket module.
     * This method interprets the command and arguments contained in the given
     * Request object and performs the corresponding action on the ticket.
     * Supported commands:
     * - "new": resets the current ticket.
     * - "add": adds a product to the ticket (requires product ID and quantity).
     * - "remove": removes a product from the ticket (requires product ID).
     * - "print": prints the current ticket contents.
     *
     * @param request the request containing the command and its arguments.
     */
    public void handleRequest(Request request) {
        String command = request.command;
        ArrayList<String> args = request.args;

        switch (command) {
            case "new":
                resetTicket();
                System.out.println("Ticket has been resetted successfully");
                return;

            case "add":
                if (args.size() < 2) {
                    System.err.println("Error: two arguments are required: id and quantity.");
                    return;
                }

                int id, quantity;
                try {
                    id = Integer.parseInt(args.get(0));
                } catch (NumberFormatException e) {
                    System.err.println("Error: the product ID must be an integer.");
                    return;
                }

                try {
                    quantity = Integer.parseInt(args.get(1));
                } catch (NumberFormatException e) {
                    System.err.println("Error: the quantity must be an integer.");
                    return;
                }

                Product product = app.getProduct(id);
                addProduct(product, quantity);
                printTicket();
                return;

            case "remove":
                if (args.isEmpty()) {
                    System.err.println("Error: one argument is required: product ID.");
                    return;
                }

                try {
                    int removeId = Integer.parseInt(args.get(0));
                    Product productToRemove = app.getProduct(removeId);
                    deleteProduct(productToRemove);
                    printTicket();
                    return;
                } catch (NumberFormatException e) {
                    System.err.println("Error: the product ID must be an integer.");
                    return;
                }

            case "print":
                printTicket();
                break;

            default:
                System.err.println("Error: command not found: " + command);
                break;
        }
    }



    private void printTicket() {
        System.out.println(ticket);
    }

    /**
     * Method to add products to ticket
     *
     * @param product Product to be added to the ticket
     * @param quantity Quantity of products wanted to add
     * @return Return -1 if the number of products in the ticket is already maximum products
     * Return -2 if it’s not maximum products yet but the quantity I want to add exceeds maximum products
     * Return 0 if product can be added
     */
    private int addProduct(Product product, int quantity) {
        if (ticket.size() >= numMaxElements) {
            // Product cannot be added due to maximum products already
            return -1;
        } else if (ticket.size() + quantity > numMaxElements) {
            return -2;
        } else {
            // Product can be added
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
    private int deleteProduct(Product productToDelete) {
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
    private int updateProduct(Product updatedProduct) {
        if (ticket.containsKey(updatedProduct)) {
            int quantity = ticket.get(updatedProduct);
            ticket.put(updatedProduct, quantity); // Override product keeping quantity
            return 0;
        }
        return -1; // Product not found
    }


    /**
     * Clears the current ticket and creates a new empty one.
     */
    private void resetTicket() {
        if (this.ticket != null) {
            this.ticket = new HashMap<>();
            this.categories = new HashMap<>();
        } else {
            System.out.println("Ticket is empty");
        }
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
        StringBuilder str = new StringBuilder();
        double totalPrice = 0;
        double totalDiscount = 0;

        for (Map.Entry<Product, Integer> entry : ticket.entrySet()) {
            Product producto = entry.getKey();
            Integer cantidad = entry.getValue();

            str.append(producto.toString());
            totalPrice += producto.getPrice() * cantidad;

            if (categories.get(producto.getCategory()) > 1) {
                double discount = producto.getPrice() - (producto.getPrice() * app.config.getDiscount(producto.getCategory()));
                str.append("**discount -").append(discount);
                totalDiscount += discount;
            }
            str.append("\n");
        }

        str.append("\nTotal price: ").append(totalPrice);
        str.append("\nTotal discount: ").append(totalDiscount);
        str.append("\nFinal price: ").append(totalPrice - totalDiscount);

        return str.toString();
    }
}
