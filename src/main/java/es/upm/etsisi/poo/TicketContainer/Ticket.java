package es.upm.etsisi.poo.TicketContainer;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.Product;
import es.upm.etsisi.poo.Requests.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private final App app;
    private HashMap<Product, Integer> ticket; // TODO is not better to use Prods id?
    private HashMap<String, Integer> categories;
    private final int numMaxElements;
    public static final String COMMAND_PREFIX = "ticket";

    public Ticket(App app) {
        this.app = app;
        this.ticket = new HashMap<>();
        this.categories = new HashMap<>();
        this.numMaxElements = app.config.getMaxProductPerTicket();

        for (String category : app.config.getCategories()) {
            categories.put(category, 0);
        }
    }

    public boolean hasProduct(int prodId) {
        return ticket.containsKey(prodId);
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
    public int handleRequest(Request request) {
        String command = request.command;
        ArrayList<String> args = request.args;

        switch (command) {
            case "new":
                return resetTicket();

            case "add":
                if (args.size() < 2) {
                    System.err.println("ERROR: two arguments are required: id and quantity.");
                    return -1;
                }

                int id, quantity;
                try {
                    id = Integer.parseInt(args.get(0));
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: the product ID must be an integer.");
                    return -1;
                }

                try {
                    quantity = Integer.parseInt(args.get(1));
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: the quantity must be an integer.");
                    return -1;
                }

                Product product = app.getProduct(id);

                if (product == null) {
                    System.err.println("ERROR: the product with id " + id + " does not exist.");
                    return -1;
                }

                return addProduct(product, quantity);

            case "remove":
                if (args.isEmpty()) {
                    System.err.println("ERROR: one argument is required: product ID.");
                    return -1;
                }

                try {
                    int removeId = Integer.parseInt(args.get(0));
                    Product productToRemove = app.getProduct(removeId);

                    int result = deleteProduct(productToRemove);
                    if (result != 0)
                    {
                        System.err.println("ERROR: Product doesn't exist.");
                    }
                    return result;
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: the product ID must be an integer.");
                    return -1;
                }

            case "print":
                printTicket();
                return 0;

            default:
                System.err.println("ERROR: Invalid command " + command);
                return -1;
        }
    }

    public int updateProduct(Product product)
    {
        if (ticket.containsKey(product))
        {
            System.out.println(this);
            return 0;
        }
        return 1;
    }

    public void printTicket() {
        System.out.println(this);
    }

    /**
     * Method to add products to ticket
     *
     * @param product  Product to be added to the ticket
     * @param quantity Quantity of products wanted to add
     * @return
     *      Return -1 if the number of products in the ticket is already maximum products
     *      Return -2 if it’s not maximum products yet but the quantity I want to add exceeds maximum products
     *      Return 0 if product can be added
     */
    private int addProduct(Product product, int quantity) {

        int totalUnits = ticket.values().stream().mapToInt(Integer::intValue).sum();

        if (totalUnits >= numMaxElements) {
            System.err.println("ERROR: maximum number of products reached.");
            return -1;
        } else if ((totalUnits + quantity) > numMaxElements) {
            System.err.println("ERROR: maximum number of products reached.");
            return -2;
        } else {
            String categoryKey = product.getCategory().toUpperCase();

            int currentQuantity = ticket.getOrDefault(product, 0);
            ticket.put(product, currentQuantity + quantity);

            int currentCategoryCount = categories.getOrDefault(categoryKey, 0);
            categories.put(categoryKey, currentCategoryCount + quantity);

            System.out.println(this);

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
            int quantity = ticket.get(productToDelete);
            ticket.remove(productToDelete);

            // Usar categoría en mayúsculas (coherente con el resto del código)
            String categoryKey = productToDelete.getCategory().toUpperCase();
            int currentCategoryCount = categories.getOrDefault(categoryKey, 0);

            // Asegurar que nunca quede negativa
            int newCount = Math.max(0, currentCategoryCount - quantity);
            categories.put(categoryKey, newCount);

            System.out.println(this);
            return 0;
        }
        return -1;
    }

    /**
     * Clears the current ticket and creates a new empty one.
     */
    private int resetTicket() {
        if (this.ticket != null) {
            this.ticket = new HashMap<>();
            this.categories = new HashMap<>();
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Builds a string representation of the ticket sorted by name.
     * The string includes the list of products with their details,
     * applied discounts by category when applicable,
     * and a summary with total price, total discount, and final price.
     *
     * @return A formatted string representation of the ticket and its summary.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        double totalPrice = 0;
        double totalDiscount = 0;

        // Counting amount of products per category
        Map<String, Integer> categoryCounts = new HashMap<>();
        for (Map.Entry<Product, Integer> entry : ticket.entrySet()) {
            String category = entry.getKey().getCategory().toUpperCase();
            int cantidad = entry.getValue();
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + cantidad);
        }

        // Sorting products by name
        ArrayList<Map.Entry<Product, Integer>> entries = new ArrayList<>(ticket.entrySet());
        entries.sort((e1, e2) -> e1.getKey().getName().compareToIgnoreCase(e2.getKey().getName()));

        for (Map.Entry<Product, Integer> entry : entries) {
            Product producto = entry.getKey();
            int cantidad = entry.getValue();
            String category = producto.getCategory().toUpperCase();
            Double discountRate = app.config.getDiscount(category);

            for (int i = 0; i < cantidad; i++) {
                str.append(producto);

                if (discountRate != null && categoryCounts.get(category) >= 2) {
                    double discount = producto.getPrice() * discountRate;
                    if (discount > 0) {
                        str.append(" **discount -").append(String.format("%.1f", discount));
                        totalDiscount += discount;
                    }
                }

                str.append("\n");
                totalPrice += producto.getPrice();
            }
        }

        str.append("Total price: ").append(String.format("%.1f", totalPrice));
        str.append("\nTotal discount: ").append(String.format("%.1f", totalDiscount));
        str.append("\nFinal price: ").append(String.format("%.1f", totalPrice - totalDiscount));

        return str.toString();
    }

}
