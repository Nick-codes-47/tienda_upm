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
import java.util.Map;

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

    // Constructor Principal: usado internamente o cuando el ID se asigna después.
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

    // Constructor Secundario: usado cuando se crea el ticket con un ID definido.
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
     *
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
     *
     * @param baseProduct Producto a añadir.
     * @param quantity    Cantidad (o número de personas para eventos).
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
     *
     * @param product     Producto personalizable.
     * @param quantity    Cantidad.
     * @param maxEditable Personalizaciones máximas permitidas.
     * @param edits       Lista de personalizaciones.
     * @return Código de resultado de addProduct.
     */
    public int addProduct(Product product, int quantity, int maxEditable, ArrayList<String> edits) {
        int result = addProduct(product, quantity);
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

    /**
     * Cierra el ticket, establece la fecha de cierre y actualiza el ID.
     */
    public void printTicket() {
        if (this.ticketState != TicketState.CERRADO) {
            this.ticketState = TicketState.CERRADO;
            this.closingDate = LocalDateTime.now();

            if (this.ticketId != null) {
                this.ticketId += "-" + this.closingDate.format(ID_CLOSING_FORMATTER);
            }
            System.out.println(this);
        }
    }

    public String getTicketId() {
        return ticketId;
    }

    /**
     * Calcula el precio total sumando el precio (unitario * cantidad) de todos los productos.
     *
     * @return El precio total antes de descuentos.
     */
    public double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (Map.Entry<BaseProduct, Integer> entry : ticket.entrySet()) {
            BaseProduct product = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += product.getPrice() * quantity;
        }
        return totalPrice;
    }

    // --- NUEVO MÉTODO AUXILIAR PARA CALCULAR EL DESCUENTO UNITARIO ---

    /**
     * Calcula el valor del descuento para una sola unidad de un producto dado.
     *
     * @param product Producto base a evaluar.
     * @return El valor del descuento (ej: 1.05) si aplica, 0.0 en caso contrario.
     */
    private double getProductDiscountValue(BaseProduct product) {
        if (product instanceof Product p) {
            Category category = p.getCategory();
            int totalCategoryUnits = categories.getOrDefault(category, 0);

            if (totalCategoryUnits >= 3) {
                double unitPrice = p.getPrice();
                double categoryDiscountRate = category.getDiscount() / 100.0;
                return unitPrice * categoryDiscountRate;
            }
        }
        return 0.0;
    }

    /**
     * Calcula el descuento total.
     * @return El descuento total.
     */
    public double calculateTotalDiscount() {
        double totalDiscount = 0.0;

        for (Map.Entry<BaseProduct, Integer> entry : ticket.entrySet()) {
            BaseProduct baseProduct = entry.getKey();
            int quantity = entry.getValue();

            // Descuento unitario del producto (0 si no aplica)
            double unitDiscount = getProductDiscountValue(baseProduct);

            // Descuento total de la línea (unitario * cantidad)
            totalDiscount += unitDiscount * quantity;
        }

        return totalDiscount;
    }

    /**
     * Calcula el precio final (Total Price - Total Discount).
     * @return El precio final.
     */
    public double calculateFinalPrice() {
        return calculateTotalPrice() - calculateTotalDiscount();
    }

    /**
     * Muestra el ID del ticket, los productos en detalle y el resumen de precios.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String displayId = (this.ticketId == null || this.ticketId.isEmpty()) ? "ID NOT YET ASSIGNED" : this.ticketId;
        sb.append("Ticket: ").append(displayId).append("\n");

        if (this.ticket.isEmpty()) {
            sb.append("No products added yet.\n");
        } else {
            for (Map.Entry<BaseProduct, Integer> entry : this.ticket.entrySet()) {
                BaseProduct product = entry.getKey();
                int quantity = entry.getValue();

                if (product instanceof Event event) {

                    double eventPriceTotal = event.getPrice() * quantity;

                    sb.append(String.format("Event Added: %s\n", event.getClass().getSimpleName()));
                    sb.append(String.format("  {class:%s, id:%s, name:'%s', price:%.1f, date of Event:%s, max people allowed:%d, actual people in event:%d}\n",
                            event.getClass().getSimpleName(),
                            event.getId(),
                            event.getName(),
                            eventPriceTotal,
                            event.getExpireDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            100,
                            quantity
                    ));

                } else {
                    double unitDiscount = getProductDiscountValue(product);
                    String discountSuffix = "";

                    if (unitDiscount > 0) {
                        discountSuffix = String.format(" **discount -%.2f", unitDiscount);
                    }

                    for (int i = 0; i < quantity; i++) {
                        sb.append(product.toString()).append(discountSuffix).append("\n");
                    }
                }
            }
        }

        sb.append("\n");
        sb.append(String.format("Total price: %.2f €\n", calculateTotalPrice()));
        sb.append(String.format("Total discount: %.2f €\n", calculateTotalDiscount()));
        sb.append(String.format("Final price: %.2f €\n", calculateFinalPrice()));

        return sb.toString();
    }
}