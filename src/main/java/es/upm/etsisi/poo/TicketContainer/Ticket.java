package es.upm.etsisi.poo.TicketContainer;

import es.upm.etsisi.poo.ProductContainer.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private static final int MAX_PRODUCTS_PER_TICKET = 100;
    private TicketState ticketState;
    private final HashMap<Integer, ProductEntry> entries;
    private final HashMap<Category, Integer> categories;
    private final LocalDateTime creationDate;
    private LocalDateTime closingDate;
    public static final String COMMAND_PREFIX = "ticket";
    private String ticketId;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");

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

    /**
     * Interfaz para que el código externo (la acción) proporcione acceso al catálogo.
     * El ticket la usa para actualizar precios sin acoplarse directamente a App/Catalog.
     */
    public interface CatalogAccessor {
        BaseProduct getProduct(int productId);
    }

    public boolean isClosed() {
        return TicketState.CERRADO == this.ticketState;
    }


    private int calculateTotalUnits() {
        return entries.values().stream().mapToInt(e -> e.amount).sum();
    }

    /**
     * Verifica si el ticket contiene el producto base dado.
     *
     * @param productId el producto base a verificar.
     * @return true si el producto se encuentra en el ticket, false en caso contrario.
     */
    public boolean hasProduct(int productId) {
        return this.entries.containsKey(productId);
    }

    public TicketState getTicketState() {
        return this.ticketState;
    }

    /**
     * Añade un producto base al ticket con la cantidad especificada.
     *
     * @param baseProduct Producto a añadir.
     * @param quantity    Cantidad (o número de personas para eventos).
     * @return 0 si es exitoso. Códigos de error: -1 (Max items), -3 (Cerrado), -4 (Planificación Evento), -5 (Evento ya añadido).
     */
    public int addProduct(BaseProduct baseProduct, int quantity) {
        if (this.ticketState == TicketState.CERRADO) {
            System.err.println("ERROR: Cannot add product. Ticket is closed (invoice printed).");
            return -3;
        }
        if (this.ticketState == TicketState.VACIO) {
            this.ticketState = TicketState.ACTIVO;
        }

        int totalUnits = calculateTotalUnits();

        if ((totalUnits + quantity) > MAX_PRODUCTS_PER_TICKET) {
            System.err.println("ERROR: maximum number of items reached.");
            return -1;
        }

        if (baseProduct instanceof Event event) {
            if (this.entries.containsKey(event.getId())) {
                System.err.println("ERROR: Cannot add the same Event (meeting/meal) twice to the same ticket.");
                return -5;
            }

            EventType type = event.getType();
            int planningHours = type.getPlanningTime();

            LocalDateTime requiredDate = LocalDateTime.now().plusHours(planningHours);
            LocalDateTime eventDate = event.getExpireDate();

            if (eventDate.isBefore(requiredDate)) {
                String typeName = EventType.toSentenceCase(type);

                System.err.printf("ERROR: Cannot add %s event. Requires a minimum planning time of %d hours before expiration (%s). Event expiration is: (%s).\n",
                        typeName, planningHours,
                        requiredDate.format(DateTimeFormatter.ofPattern(String.valueOf(DATE_TIME_FORMATTER))),
                        eventDate.format(DateTimeFormatter.ofPattern(String.valueOf(DATE_TIME_FORMATTER))));
                return -4;
            }
        }

        ProductEntry e = entries.getOrDefault(baseProduct.getId(), null);
        int currentQuantity = (e == null ? 0 : e.amount);

        if (e != null) {
            ProductEntry updatedEntry = new ProductEntry(
                    e.product,
                    currentQuantity + quantity,
                    e.unitPriceSnapshot,
                    e.categorySnapshot,
                    e.productNameSnapshot,
                    e.productIdSnapshot
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
     * Metodo sobrecargado para productos personalizables (que reciben personalizaciones y cantidad).
     *
     * @param edits Array de personalizaciones.
     * @return Código de resultado de addProduct.
     */
    public int addProduct(CustomProduct product, int quantity, ArrayList<String> edits) throws BaseProduct.InvalidProductException {
        product.setPersonalizations(edits);
        return addProduct(product, quantity);
    }

    public int deleteProduct(int prodId) {
        if (this.ticketState == TicketState.CERRADO) {
            System.err.println("ERROR: Cannot delete product. Ticket is closed (invoice printed).");
            return -3;
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

            System.out.println(this);
            return 0;
        }
        return -1;
    }

    /**
     * Actualiza el unitPriceSnapshot de las entradas de producto con el precio actual del catálogo.
     * Solo debe llamarse cuando el ticket está ABIERTO.
     * @param accessor Objeto que permite acceder a los productos del catálogo.
     */
    public void updatePrices(CatalogAccessor accessor) {
        if (this.ticketState == TicketState.CERRADO) {
            return;
        }

        HashMap<Integer, ProductEntry> updatedEntries = new HashMap<>();

        for (Map.Entry<Integer, ProductEntry> entry : entries.entrySet()) {
            int productId = entry.getKey();
            ProductEntry currentEntry = entry.getValue();

            BaseProduct currentCatalogProduct = accessor.getProduct(productId);

            if (currentCatalogProduct != null) {
                if (currentCatalogProduct.getPrice() != currentEntry.unitPriceSnapshot) {

                    ProductEntry updatedEntry = new ProductEntry(
                            currentEntry.product,
                            currentEntry.amount,
                            currentCatalogProduct.getPrice(),
                            currentEntry.categorySnapshot,
                            currentEntry.productNameSnapshot,
                            currentEntry.productIdSnapshot
                    );
                    updatedEntries.put(productId, updatedEntry);
                    continue;
                }
            }

            updatedEntries.put(productId, currentEntry);
        }

        this.entries.clear();
        this.entries.putAll(updatedEntries);
    }

    /**
     * Cierra el ticket, establece la fecha de cierre y actualiza el ID.
     * Se permite la reimpresión sin modificar el estado/ID.
     */
    public void printTicket() {
        if (this.ticketState != TicketState.CERRADO) {
            this.ticketState = TicketState.CERRADO;
            this.closingDate = LocalDateTime.now();

            if (this.ticketId != null) {
                this.ticketId += "-" + this.closingDate.format(DATE_TIME_FORMATTER);
            }
        }
        System.out.println(this);
    }

    public String getTicketId() {
        return ticketId;
    }

    /**
     * Calcula el precio total sumando el precio (unitario * cantidad) de todos los productos.
     * Usa unitPriceSnapshot.
     *
     * @return El precio total antes de descuentos.
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
     *
     * @return El descuento total.
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
     *
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

                    String fixedProductString = String.format("{class:Product, id:%d, name:'%s', category:%s, price:%.2f}",
                            entry.productIdSnapshot,
                            entry.productNameSnapshot,
                            entry.categorySnapshot.name(),
                            entry.unitPriceSnapshot);


                    for (int i = 0; i < quantity; i++) {
                        sb.append(fixedProductString).append(discountSuffix).append("\n");
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
     * CLASE ANIDADA MODIFICADA: Almacena todos los atributos esenciales (ID, Nombre, Categoría, Precio)
     * como snapshots para garantizar la inmutabilidad de la línea de ticket cuando se modifica
     * un producto disponible en un ticket cuando previamente se ha cerrado.
     */
    private static class ProductEntry {
        public final BaseProduct product;
        public final int amount;

        public final double unitPriceSnapshot;
        public final Category categorySnapshot;
        public final String productNameSnapshot;
        public final int productIdSnapshot;


        public ProductEntry(BaseProduct product, int amount) {
            this.product = product;
            this.amount = amount;

            this.unitPriceSnapshot = product.getPrice();
            this.productNameSnapshot = product.getName();
            this.productIdSnapshot = product.getId();

            if (product instanceof Product p) {
                this.categorySnapshot = p.getCategory();
            } else {
                this.categorySnapshot = null;
            }
        }

        public ProductEntry(BaseProduct product, int amount, double unitPriceSnapshot,
                            Category categorySnapshot, String productNameSnapshot, int productIdSnapshot) {
            this.product = product;
            this.amount = amount;
            this.unitPriceSnapshot = unitPriceSnapshot;
            this.categorySnapshot = categorySnapshot;
            this.productNameSnapshot = productNameSnapshot;
            this.productIdSnapshot = productIdSnapshot;
        }
    }
}