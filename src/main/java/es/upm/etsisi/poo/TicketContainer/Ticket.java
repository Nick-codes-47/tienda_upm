package es.upm.etsisi.poo.TicketContainer;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.Category;
import es.upm.etsisi.poo.ProductContainer.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private final App app;
    private TicketState ticketState;
    private HashMap<BaseProduct, Integer> ticket;
    private HashMap<Category, Integer> categories;
    private final int numMaxElements;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;
    public static final String COMMAND_PREFIX = "ticket";

    private String ticketId;


    private Ticket(App app) {
        this.app = app;
        this.ticket = new HashMap<>();
        this.categories = new HashMap<>();
        this.numMaxElements = app.config.getMaxProductPerTicket();
        this.ticketState = TicketState.ACTIVO;

        for (Category category : Category.values()) {
            categories.put(category, 0);
        }
    }

    public Ticket(App app, String ticketId) {
        this(app);
        this.ticketId = ticketId;
    }

    public boolean hasProduct(int prodId) {
        BaseProduct p = app.catalog.getProduct(prodId);
        return p != null && ticket.containsKey(p);
    }

    public boolean isClosed() {
        return TicketState.CERRADO == this.ticketState;
    }

    private int calculateTotalUnits() {
        return ticket.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void printTicket() {
        if (this.ticketState != TicketState.CERRADO) {
            this.ticketState = TicketState.CERRADO;
        }

        System.out.println(this);
    }

    /**
     * Añade productos al ticket. Solo permitido si el ticket está ACTIVO.
     */
    public int addProduct(Product product, int quantity) {
        // 🆕 NUEVO CONTROL: No se puede añadir si está cerrado
        if (this.ticketState == TicketState.CERRADO) {
            System.err.println("ERROR: Cannot add product. Ticket is closed (invoice printed).");
            return -3; // Nuevo código de error para ticket cerrado
        }

        int totalUnits = calculateTotalUnits();

        if (totalUnits >= numMaxElements) {
            System.err.println("ERROR: maximum number of products reached.");
            return -1;
        } else if ((totalUnits + quantity) > numMaxElements) {
            System.err.println("ERROR: maximum number of products reached.");
            return -2;
        } else {
            Category categoryKey = product.getCategory();

            int currentQuantity = ticket.getOrDefault(product, 0);
            ticket.put(product, currentQuantity + quantity);

            int currentCategoryCount = categories.getOrDefault(categoryKey, 0);
            categories.put(categoryKey, currentCategoryCount + quantity);

            System.out.println(this);
            return 0;
        }
    }

    /**
     * Sobrecarga de addProduct. Hereda el control de estado.
     */
    public int addProduct(Product product, int quantity, int maxEditable, ArrayList<String> edits) {
        int result = addProduct(product, quantity);
        if (result != 0) {
            return result;
        }
        return 0;
    }

    /**
     * Elimina productos del ticket. Solo permitido si el ticket está ACTIVO.
     */
    public int deleteProduct(Product productToDelete) {
        // 🆕 NUEVO CONTROL: No se puede eliminar si está cerrado
        if (this.ticketState == TicketState.CERRADO) {
            System.err.println("ERROR: Cannot delete product. Ticket is closed (invoice printed).");
            return -3;
        }

        if (ticket.containsKey(productToDelete)) {
            int quantity = ticket.get(productToDelete);
            ticket.remove(productToDelete);

            Category categoryKey = productToDelete.getCategory();
            int currentCategoryCount = categories.getOrDefault(categoryKey, 0);

            int newCount = Math.max(0, currentCategoryCount - quantity);
            categories.put(categoryKey, newCount);

            System.out.println(this);
            return 0;
        }
        return -1;
    }

    /**
     * Reinicia el ticket. Solo permitido si el ticket no está CERRADO.
     * NO SE SI SE SIGUE PUDIENDO LLAMAR A ESTE METODO

    private int resetTicket() {
        // 🆕 NUEVO CONTROL: No se puede abrir/reiniciar si está cerrado
        if (this.ticketState == TicketState.CERRADO) {
            System.err.println("ERROR: Cannot reset ticket. Ticket is permanently closed.");
            return -3;
        }

        if (this.ticket != null) {
            this.ticket = new HashMap<>();
            this.categories = new HashMap<>();
            this.ticketState = TicketState.ACTIVO;
            this.closingDate = null;
            return 0;
        } else {
            return -1;
        }
    }
     */

    public String getTicketId() {
        return ticketId;
    }

    public int updateProduct(Product product) {
        if (ticket.containsKey(product)) {
            System.out.println(this);
            return 0;
        }
        return 1;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        double totalPrice = 0;
        double totalDiscount = 0;

        Map<Category, Integer> categoryCounts = new HashMap<>();
        for (Map.Entry<BaseProduct, Integer> entry : ticket.entrySet()) {
            Category category = entry.getKey().getCategory();
            int cantidad = entry.getValue();
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + cantidad);
        }

        ArrayList<Map.Entry<BaseProduct, Integer>> entries = new ArrayList<>(ticket.entrySet());
        entries.sort((e1, e2) -> e1.getKey().getName().compareToIgnoreCase(e2.getKey().getName()));

        for (Map.Entry<BaseProduct, Integer> entry : entries) {
            BaseProduct producto = entry.getKey();
            int cantidad = entry.getValue();
            Category category = producto.getCategory();
            Double discountRate = app.config.getDiscount(category.name());

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

        str.append("Ticket ID: ").append(ticketId).append(" | Estado: ").append(ticketState);
        if (closingDate != null) {
            str.append(" | Cierre: ").append(closingDate.toString());
        }

        str.append("\nTotal price: ").append(String.format("%.1f", totalPrice));
        str.append("\nTotal discount: ").append(String.format("%.1f", totalDiscount));
        str.append("\nFinal price: ").append(String.format("%.1f", totalPrice - totalDiscount));

        return str.toString();
    }
}