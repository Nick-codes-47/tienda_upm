package es.upm.etsisi.poo.TicketContainer;

import es.upm.etsisi.poo.ProductContainer.ProductTypes.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.CustomProduct;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.Event;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.Product;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.ProductEnums.Category;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.ProductEnums.EventType;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Ticket {
    private static final int MAX_PRODUCTS_PER_TICKET = 100;
    private TicketState ticketState;
    private final HashMap<Integer, ProductEntry> entries;
    private final HashMap<Category, Integer> categories;
    private final LocalDateTime creationDate;
    private LocalDateTime closingDate;
    public static final String COMMAND_PREFIX = "ticket";
    private String ticketId;
    private String printedOutput = null;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");

    private static final String DATE_PATTERN_STRING = "yy-MM-dd-HH:mm";


    Ticket() {
        this.entries = new HashMap<>();
        this.categories = new HashMap<>();
        this.ticketState = TicketState.VACIO;
        this.creationDate = LocalDateTime.now();

        for (Category category : Category.values()) {
            categories.put(category, 0);
        }
    }

    public Ticket(String ticketId) {
        this();
        this.ticketId = creationDate.format(DATE_TIME_FORMATTER) + "-" + ticketId;
    }

    public boolean isClosed() {
        return TicketState.CERRADO == this.ticketState;
    }


    private int calculateTotalUnits() {
        return entries.values().stream().mapToInt(e -> e.amount).sum();
    }

    public boolean hasProduct(int productId) {
        return this.entries.containsKey(productId);
    }

    public TicketState getTicketState() {
        return this.ticketState;
    }

    /**
     * Add a product to the ticket with the quantity introduced
     * @param baseProduct product to be added
     * @param quantity how many products to be added
     * @return  0 if product is added correctly
     *         -1 ticket state is closed
     *         -2 maximum number of items reached
     *         -3 Cannot add the same Event (meeting/meal) twice to the same ticket
     *         -4 Event requires minimum time to be planned
     *         -5 Error in the number of people in event
     */
    public int addProduct(BaseProduct baseProduct, int quantity) {
        if (this.ticketState == TicketState.CERRADO) {
            return -1;
        }
        if (this.ticketState == TicketState.VACIO) {
            this.ticketState = TicketState.ACTIVO;
        }

        int totalUnits = calculateTotalUnits();

        if ((totalUnits + quantity) > MAX_PRODUCTS_PER_TICKET) {
            System.err.println("ERROR: maximum number of items reached.");
            return -2;
        }

        if (baseProduct instanceof Event event) {
            if (this.entries.containsKey(event.getId())) {
                System.err.println("ERROR: Cannot add the same Event (meeting/meal) twice to the same ticket.");
                return -3;
            }

            EventType type = event.getType();
            int planningHours = type.getPlanningTime();

            LocalDateTime requiredDate = LocalDateTime.now().plusHours(planningHours);
            LocalDateTime eventDate = event.getExpireDate();

            if (eventDate.isBefore(requiredDate)) {
                String typeName = EventType.toSentenceCase(type);

                System.err.printf("ERROR: Cannot add %s event. Requires a minimum planning time of %d hours before expiration (%s). Event expiration is: (%s).\n",
                        typeName, planningHours,
                        requiredDate.format(DATE_TIME_FORMATTER),
                        eventDate.format(DATE_TIME_FORMATTER)
                );
                return -4;
            }
            try {
                event.setActualPeople(quantity);
            } catch (BaseProduct.InvalidProductException e) {
                return -5;
            }
        }

        ProductEntry e = entries.getOrDefault(baseProduct.getId(), null);
        int currentQuantity = (e == null ? 0 : e.amount);

        if (e != null) {
            ProductEntry updatedEntry = new ProductEntry(
                    e.product,
                    currentQuantity + quantity,
                    e.unitPriceSnapshot,
                    e.categorySnapshot
            );
            entries.put(baseProduct.getId(), updatedEntry);
        } else {
            entries.put(baseProduct.getId(), new ProductEntry(baseProduct, quantity));
        }

        if (baseProduct instanceof Product product) {
            Category categoryKey = product.getCategory();
            int currentCategoryCount = categories.getOrDefault(categoryKey, 0);
            categories.put(categoryKey, currentCategoryCount + quantity);
        }

        System.out.println(this);
        return 0;
    }

    /**
     * Override method needed to add a custom product into the ticket
     * @param product custom product to be added
     * @param quantity quantity of products to be added
     * @param edits customizations for the product
     * @return -6 Maximum product customizations reached
     */
    public int addProduct(CustomProduct product, int quantity, ArrayList<String> edits) {
        try {
            product.setPersonalizations(edits);
        } catch (BaseProduct.InvalidProductException e) {
            return -6;
        }
        return addProduct(product, quantity);
    }

    /**
     * Method to delete a product with the product id
     * @param prodId id of the product to be removed from ticket
     * @return 0 product removed successfully from ticket
     *        -1 ticket state is closed
     *        -2 product not found in ticket
     */
    public int deleteProduct(int prodId) {
        if (this.ticketState == TicketState.CERRADO) {
            return -1;
        }

        if (entries.containsKey(prodId)) {
            ProductEntry entryToRemove = entries.get(prodId);
            int quantity = entryToRemove.amount;
            entries.remove(prodId);

            if (entryToRemove.product instanceof Product product) {
                Category categoryKey = product.getCategory();
                int currentCategoryCount = categories.getOrDefault(categoryKey, 0);

                int newCount = Math.max(0, currentCategoryCount - quantity);
                categories.put(categoryKey, newCount);
            }
            return 0;
        }
        return -2;
    }

    /**
     * Permite modificar un atributo del objeto BaseProduct referenciado y,
     * crucialmente, actualiza el snapshot del precio si el campo modificado es 'price'.
     */
    public void updateProduct(BaseProduct product, Field field, Object newValueConverted)  throws IllegalAccessException{

        if (this.ticketState == TicketState.CERRADO) {
            System.err.println("ERROR: Cannot update product details. Ticket is closed (invoice printed).");
            return;
        }

        ProductEntry productEntry = this.entries.get(product.getId());

        if (productEntry != null) {
            BaseProduct baseProduct = productEntry.product;
            field.set(baseProduct, newValueConverted); // Actualiza la referencia del producto

            if (field.getName().toLowerCase().equals("price")) {

                ProductEntry updatedEntry = new ProductEntry(
                        productEntry.product,
                        productEntry.amount,
                        baseProduct.getPrice(),
                        productEntry.categorySnapshot
                );
                this.entries.put(product.getId(), updatedEntry);
            }
        }
    }

    /**
     * Cierra el ticket y lo imprime. Si ya está cerrado, imprime la versión cacheada.
     */
    public void printTicket() {
        if (this.printedOutput != null) {
            System.out.println(this.printedOutput);
            return;
        }

        if (this.ticketState != TicketState.CERRADO) {
            this.ticketState = TicketState.CERRADO;
            this.closingDate = LocalDateTime.now();

            if (this.ticketId != null) {
                this.ticketId = this.ticketId.substring(this.ticketId.length()-5) + "-" + this.closingDate.format(DATE_TIME_FORMATTER);
            }
        }

        this.printedOutput = this.toString();

        System.out.println(this.printedOutput);
    }

    public String getTicketId() {
        return ticketId;
    }

    /**
     * Calcula el precio total sumando el precio (unitario * cantidad) de todos los productos.
     * Usa unitPriceSnapshot.
     */
    public double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (ProductEntry entry : entries.values()) {
            int quantity = entry.amount;
            totalPrice += entry.unitPriceSnapshot * quantity;
        }
        return totalPrice;
    }

    /**
     * Calcula el valor del descuento para una sola unidad de un producto dado.
     * Usa unitPriceSnapshot y categorySnapshot.
     */
    private double getProductDiscountValue(ProductEntry entry) {
        Category category = entry.categorySnapshot;
        if (category != null) {
            int totalCategoryUnits = categories.getOrDefault(category, 0);

            if (totalCategoryUnits >= 3) {
                double unitPrice = entry.unitPriceSnapshot;
                double categoryDiscountRate = category.getDiscount() / 100.0;
                return unitPrice * categoryDiscountRate;
            }
        }
        return 0.0;
    }

    /**
     * Calcula el descuento total.
     */
    public double calculateTotalDiscount() {
        double totalDiscount = 0.0;

        for (ProductEntry entry : entries.values()) {
            int quantity = entry.amount;
            double unitDiscount = getProductDiscountValue(entry);
            totalDiscount += unitDiscount * quantity;
        }

        return totalDiscount;
    }

    /**
     * Calcula el precio final (Total Price - Total Discount).
     */
    public double calculateFinalPrice() {
        return calculateTotalPrice() - calculateTotalDiscount();
    }

    /**
     * Shows ticket id, with product details and price summary
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Ticket: ").append(this.ticketId).append("\n");

        if (this.entries.isEmpty()) {
            sb.append("No products added yet.\n");
        } else {
            for (ProductEntry entry : this.entries.values()) {
                BaseProduct product = entry.product;
                int quantity = entry.amount;

                if (product instanceof Event event) {
                    sb.append(event).append("\n");
                } else {
                    double unitDiscount = getProductDiscountValue(entry);
                    String discountSuffix = "";
                    if (unitDiscount > 0) {
                        discountSuffix = String.format(" **discount -%.2f", unitDiscount);
                    }

                    String productLine;

                    if (product instanceof CustomProduct customProduct) {
                        productLine = customProduct.toString();
                    } else {
                        Product p = (Product) product;
                        productLine = String.format("{class:Product, id:%d, name:'%s', category:%s, price:%.2f}",
                                p.getId(), p.getName(), entry.categorySnapshot.name(), entry.unitPriceSnapshot);
                    }

                    for (int i = 0; i < quantity; i++) {
                        sb.append(productLine).append(discountSuffix).append("\n");
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

    /**
     * CLASE ANIDADA: Mantiene solo los Snapshots de Precio y Categoría.
     */
    private static class ProductEntry {
        public final BaseProduct product;
        public final int amount;

        public final double unitPriceSnapshot;
        public final Category categorySnapshot;

        public ProductEntry(BaseProduct product, int amount) {
            this.product = product;
            this.amount = amount;
            this.unitPriceSnapshot = product.getPrice();

            if (product instanceof Product p) {
                this.categorySnapshot = p.getCategory();
            } else {
                this.categorySnapshot = null;
            }
        }

        public ProductEntry(BaseProduct product, int amount, double unitPriceSnapshot, Category categorySnapshot) {
            this.product = product;
            this.amount = amount;
            this.unitPriceSnapshot = unitPriceSnapshot;
            this.categorySnapshot = categorySnapshot;
        }
    }
}