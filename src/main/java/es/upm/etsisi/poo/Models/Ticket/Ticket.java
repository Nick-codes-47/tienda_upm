package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Product.Products.Product;
import es.upm.etsisi.poo.Models.Product.Products.EventProduct;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.Category;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Ticket <T extends GoodsProduct> {

    public Ticket(String ticketID) {
        this.entries = new HashMap<>();
        this.categories = new HashMap<>();
        this.ticketState = TicketState.VACIO;
        this.creationDate = LocalDateTime.now();
        this.ticketId = creationDate.format(DATE_TIME_FORMATTER) + "-" + ticketID;
    }

    public boolean hasProduct(int productId) {
        return this.entries.containsKey(productId);
    }

    public TicketState getTicketState() {
        return this.ticketState;
    }

    private void checkCapacity(int added) {
        if ((totalUnits + added) > MAX_PRODUCTS_PER_TICKET) {
            System.err.println("ERROR: maximum number of items reached.");
        } // TODO add exception
    }

    private void updateCategoryCount(GoodsProduct goodsProduct, int amount) {
        if (goodsProduct instanceof Product product) {
            Category category = product.getCategory();
            categories.put(
                    category,
                    categories.getOrDefault(category, 0) + amount);
        }
    }

    /**
     * Add a product to the ticket with the amount introduced
     * @param product to be added
     * @param amount how many products to be added
     * @return  0 if product is added correctly
     * -1 ticket state is closed
     * -2 maximum number of items reached
     * -3 Cannot add the same Event (meeting/meal) twice to the same ticket
     * -4 Event requires minimum time to be planned
     * -5 Error in the number of people in event
     */
    public int add(T product, int amount) {
        if (this.ticketState == TicketState.CERRADO) {
            return -1;
        }

        if (this.ticketState == TicketState.VACIO) {
            this.ticketState = TicketState.ACTIVO;
        }

        checkCapacity(amount);

        Entry entry = entries.get(product.getId());
        if (entry == null) {
            entry = new Entry(product.clone(), amount);
        } else {
            entry.amount += amount;
        }

        entries.put(product.getId(), entry);
        totalUnits += entry.amount;

        updateCategoryCount(product, amount) ;

        System.out.println(this);
        return 0;
    }

    /**
     * Method to delete a product with the product id
     * @param prodId id of the product to be removed from ticket
     * @return 0 product removed successfully from ticket
     * -1 ticket state is closed
     * -2 product not found in ticket
     */
    public int delete(int prodId) {
        if (this.ticketState == TicketState.CERRADO) {
            return -1;
        }

        if (entries.containsKey(prodId)) {
            Entry entryToRemove = entries.get(prodId);
            int quantity = entryToRemove.amount;

            if (entryToRemove.product instanceof Product product) {
                Category categoryKey = product.getCategory();
                int currentCategoryCount = categories.getOrDefault(categoryKey, 0);

                int newCount = Math.max(0, currentCategoryCount - quantity);
                categories.put(categoryKey, newCount);
                totalUnits -= quantity;
            }

            entries.remove(prodId);
            return 0;
        }
        return -2;
    }

    /**
     * Permite modificar un atributo del objeto BaseProduct referenciado y,
     * crucialmente, actualiza el snapshot del precio si el campo modificado es 'price'.
     */
    public int update(T product) {
        if (this.ticketState == TicketState.CERRADO) {
            return -1;
        }

        Entry entry = this.entries.get(product.getId());
        if (entry != null) {
            entry.product = product.clone();
        }

        return 0;
    }

    /**
     * Cierra el ticket y lo imprime. Si ya está cerrado, imprime la versión cacheada.
     */
    public void print() {
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
    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (Entry entry : entries.values()) {
            int quantity = entry.amount;
            totalPrice += entry.product.getPrice() * quantity;
        }
        return totalPrice;
    }

    /**
     * Calcula el valor del descuento para una sola unidad de un producto dado.
     * Usa unitPriceSnapshot y categorySnapshot.
     */
    private double getProductDiscountValue(Entry entry) {
        if (entry.product instanceof Product product) {
            Category category = product.getCategory();
            if (category != null) {
                int totalCategoryUnits = categories.getOrDefault(category, 0);

                if (totalCategoryUnits >= 2) {
                    double unitPrice = product.getPrice();
                    double categoryDiscountRate = category.getDiscount() / 100.0;
                    return unitPrice * categoryDiscountRate;
                }
            }
        }
        return 0.0;
    }

    /**
     * Calcula el descuento total.
     */
    private double calculateTotalDiscount() {
        double totalDiscount = 0.0;

        for (Entry entry : entries.values()) {
            int quantity = entry.amount;
            double unitDiscount = getProductDiscountValue(entry);
            totalDiscount += unitDiscount * quantity;
        }

        return totalDiscount;
    }

    /**
     * Calcula el precio final (Total Price - Total Discount).
     */
    private double calculateFinalPrice() {
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
            for (Entry entry : this.entries.values()) {
                GoodsProduct product = entry.product;
                int quantity = entry.amount;

                if (product instanceof EventProduct event) {
                    sb.append(event).append("\n");
                } else {
                    double unitDiscount = getProductDiscountValue(entry);
                    String discountSuffix = "";
                    if (unitDiscount > 0) {
                        discountSuffix = String.format(" **discount -%.2f", unitDiscount);
                    }

                    String productLine;

                    if (product instanceof Product customProduct) {
                        productLine = customProduct.toString();
                    } else {
                        Product p = (Product) product;
                        productLine = String.format("{class:Product, id:%d, name:'%s', category:%s, price:%.2f}",
                                p.getId(), p.getName());
                    } // TODO show the product's price and category when ticket is open. Snapshot only when it's closed

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

    private static final int MAX_PRODUCTS_PER_TICKET = 100;
    private int totalUnits = 0;

    private TicketState ticketState;
    private final HashMap<Integer, TicketEntry<>> entries;
    private final HashMap<Category, Integer> categories;
    private final LocalDateTime creationDate;
    private LocalDateTime closingDate;
    private String ticketId;
    private String printedOutput = null;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
}