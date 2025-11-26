package es.upm.etsisi.poo.TicketContainer;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.Category;
import es.upm.etsisi.poo.ProductContainer.Product;
import es.upm.etsisi.poo.ProductContainer.Event;
import es.upm.etsisi.poo.ProductContainer.EventType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Ticket {
    private static final int MAX_PRODUCTS_PER_TICKET = 100;
    private final App app;
    private TicketState ticketState;
    private HashMap<BaseProduct, Integer> ticket;
    private HashMap<Category, Integer> categories;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;
    public static final String COMMAND_PREFIX = "ticket";
    private String ticketId;

    private static final DateTimeFormatter ID_CLOSING_FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");

    enum TicketState {
        ACTIVO,
        CERRADO
    }

    Ticket(App app) {
        this.app = app;
        this.ticket = new HashMap<>();
        this.categories = new HashMap<>();
        this.ticketState = TicketState.ACTIVO;
        this.creationDate = LocalDateTime.now();

        for (Category category : Category.values()) {
            categories.put(category, 0);
        }
    }

    public Ticket(App app, String ticketId) {
        this(app);
        this.ticketId = ticketId;
    }

    public boolean isClosed() {
        return TicketState.CERRADO == this.ticketState;
    }

    private int calculateTotalUnits() {
        return ticket.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Verifica si el ticket contiene el producto base dado.
     * @param product el producto base a verificar.
     * @return true si el producto se encuentra en el ticket, false en caso contrario.
     */
    public boolean hasProduct(BaseProduct product) {
        return this.ticket.containsKey(product);
    }

    /**
     * Añade un producto base al ticket con la cantidad especificada.
     * Implementa las restricciones de aforo, tiempo de planificación de eventos,
     * y la restricción de que un Evento (reunión/comida) solo puede añadirse una vez.
     * @param baseProduct Producto a añadir.
     * @param quantity Cantidad (o número de personas para eventos).
     * @return 0 si es exitoso. Códigos de error: -1 (Max items), -3 (Cerrado), -4 (Planificación Evento), -5 (Evento ya añadido).
     */
    public int addProduct(BaseProduct baseProduct, int quantity) {
        if (this.ticketState == TicketState.CERRADO) {
            System.err.println("ERROR: Cannot add item. Ticket is closed (invoice printed).");
            return -3;
        }

        int totalUnits = calculateTotalUnits();

        if ((totalUnits + quantity) > MAX_PRODUCTS_PER_TICKET) {
            System.err.println("ERROR: maximum number of items reached.");
            return -1;
        }

        if (baseProduct instanceof Event event) {
            if (this.ticket.containsKey(event)) {
                System.err.println("ERROR: Cannot add the same Event (meeting/meal) twice to the same ticket.");
                return -5;
            }

            EventType type = event.getType();
            int planningHours = type.getPlanningTime();

            LocalDateTime requiredDate = LocalDateTime.now().plusHours(planningHours);
            LocalDateTime eventDate = event.getExpireDate();

            if (eventDate.isBefore(requiredDate)) {
                String typeName = EventType.toSentenceCase(type);

                System.err.printf("ERROR: Cannot add %s event. Requires a minimum planning time of %d hours before expiration (%s).\n",
                        typeName, planningHours, requiredDate.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")));
                return -4;
            }
        }

        int currentQuantity = ticket.getOrDefault(baseProduct, 0);
        ticket.put(baseProduct, currentQuantity + quantity);

        if (baseProduct instanceof Product product) {
            Category categoryKey = product.getCategory();
            int currentCategoryCount = categories.getOrDefault(categoryKey, 0);
            categories.put(categoryKey, currentCategoryCount + quantity);
        }

        System.out.println(this);
        return 0;
    }

    /**
     * Variante para productos personalizables (que reciben personalizaciones y cantidad).
     * @param product Producto personalizable.
     * @param quantity Cantidad.
     * @param maxEditable Personalizaciones máximas permitidas.
     * @param edits Lista de personalizaciones.
     * @return Código de resultado de addProduct.
     */
    public int addProduct(Product product, int quantity, int maxEditable, ArrayList<String> edits) {
        // En un escenario real, aquí se verificaría y aplicaría la personalización.
        // Por ahora, solo se llama al método base addProduct(BaseProduct, quantity).

        // TODO: En un futuro, añadir aquí la lógica para validar edits vs maxEditable

        int result = addProduct((BaseProduct) product, quantity);
        return result;
    }

    public int deleteProduct(BaseProduct productToDelete) {
        if (this.ticketState == TicketState.CERRADO) {
            System.err.println("ERROR: Cannot delete product. Ticket is closed (invoice printed).");
            return -3;
        }

        if (ticket.containsKey(productToDelete)) {
            int quantity = ticket.get(productToDelete);
            ticket.remove(productToDelete);

            if (productToDelete instanceof Product product) {
                Category categoryKey = product.getCategory();
                int currentCategoryCount = categories.getOrDefault(categoryKey, 0);

                int newCount = Math.max(0, currentCategoryCount - quantity);
                categories.put(categoryKey, newCount);
            }

            System.out.println(this);
            return 0;
        }
        return -1;
    }

    public int deleteProduct(Product productToDelete) {
        return deleteProduct((BaseProduct) productToDelete);
    }

    /**
     * Cierra el ticket, establece la fecha de cierre y actualiza el ID.
     */
    public void printTicket() {
        if (this.ticketState != TicketState.CERRADO) {
            this.ticketState = TicketState.CERRADO;
            this.closingDate = LocalDateTime.now();

            this.ticketId += "-" + this.closingDate.format(ID_CLOSING_FORMATTER);
        }
    }

    public String getTicketId() {
        return ticketId;
    }
}