package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Core.AppException;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.EventProduct;
import es.upm.etsisi.poo.Models.Product.Products.Product;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.Category;
import es.upm.etsisi.poo.Models.Product.Products.ServiceProduct;

import java.util.HashMap;

public class Ticket<ProductType extends BaseProduct> {

    public Ticket(TicketID ID) {
        this.ID = ID;
        this.entries = new HashMap<>();
        this.categories = new HashMap<>();
        this.ticketState = TicketState.VACIO;
    }

    public Ticket(int ID) throws AppException {
        this(new TicketID(ID));
    }

    public Ticket(Ticket<ProductType> other) { // TODO shallow copies instead of deep copy
        this(other.ID);
        this.ticketState = other.ticketState;
        this.entries.putAll(other.entries);
        this.categories.putAll(other.categories);
        this.totalUnits = other.totalUnits;
    }

    public TicketID getID() { return ID; }

    public TicketState getTicketState() {
        return this.ticketState;
    }

    public boolean hasProduct(ProductID productId) {
        return this.entries.containsKey(productId);
    }

    private void checkCapacity(int added) {
        if ((totalUnits + added) > MAX_PRODUCTS_PER_TICKET) {
            System.err.println("ERROR: maximum number of items reached.");
        } // TODO add exception
    }

    private int add(TicketEntry<ProductType> entry) throws AppException {
        if (this.ticketState == TicketState.CERRADO) {
            return -1;
        }

        if (this.ticketState == TicketState.VACIO) {
            this.ticketState = TicketState.ACTIVO;
        }

        checkCapacity(entry.getProductCount());

        entries.put(entry.product.getID(), entry);
        totalUnits += entry.getProductCount();

        if (entry instanceof ProductEntry productEntry) {
            Product product = productEntry.product;
            Category category = product.getCategory();
            int total = categories.getOrDefault(category, 0);
            categories.put(category, total + entry.getProductCount());
        }

        System.out.println(this);
        return 0;
    }

    public int add(ProductType product) throws AppException {
        TicketEntry<ProductType> entry;
        if (product instanceof ServiceProduct service)
            entry = (TicketEntry<ProductType>) new ServiceEntry(service);
        else throw new AppException("");

        return add(entry);
    }

    public int add(ProductType baseProduct, int amount) throws AppException {
        TicketEntry<ProductType> entry;
        switch (baseProduct) {
            case Product product:
                entry = (TicketEntry<ProductType>) new ProductEntry(product);
                ((ProductEntry) entry).amount = amount;
                break;
            case EventProduct event:
                entry = (TicketEntry<ProductType>) new EventEntry(event);
                ((EventEntry) entry).setActualPeople(amount);
                break;
            case null, default:
                throw new AppException("");
        };

        return add(entry);
    }

    public int delete(ProductID ID) {
        if (this.ticketState == TicketState.CERRADO) {
            return -1;
        }

        TicketEntry<?> entry = entries.get(ID);
        if (entry == null)
            return -2;

        if (entry instanceof ProductEntry productEntry) {
            Product product = productEntry.product;
            Category categoryKey = product.getCategory();
            int currentCategoryCount = categories.getOrDefault(categoryKey, 0);
            int newCount = Math.max(0, currentCategoryCount - productEntry.getProductCount());
            categories.put(categoryKey, newCount);
            totalUnits -= productEntry.getProductCount();
        }

        entries.remove(ID);
        return 0;
    }

    public int update(BaseProduct product) {
        if (this.ticketState == TicketState.CERRADO) {
            return -1;
        }

        TicketEntry<ProductType> entry = this.entries.get(product.getID());
        if (entry != null) {
            entry.product = (ProductType) product.clone();
        }

        return 0;
    }

    /**
     * Cierra el ticket y lo imprime. Si ya está cerrado, imprime la versión cacheada.
     */
    public void print() {

        if (this.ticketState != TicketState.CERRADO) {
            this.ticketState = TicketState.CERRADO;
            ID.close();
        }

        System.out.println(this);
    }

    /**
     * Calcula el precio total sumando el precio (unitario * cantidad) de todos los productos.
     * Usa unitPriceSnapshot.
     */
    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (TicketEntry<ProductType> entry : entries.values()) {
            totalPrice += entry.getPrice();
        }
        return totalPrice;
    }

    /**
     * Calcula el valor del descuento para una sola unidad de un producto dado.
     * Usa unitPriceSnapshot y categorySnapshot.
     */
    private double getProductDiscountValue(ProductType baseProduct) {
        if (baseProduct instanceof Product product) {
            Category category = product.getCategory();

            if (category != null) {
                int totalCategoryUnits = categories.getOrDefault(category, 0);

                if (totalCategoryUnits >= MIN_UNITS_FOR_DISCOUNT) {
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

        for (TicketEntry<ProductType> entry : entries.values()) {
            int quantity = entry.getProductCount();
            double unitDiscount = getProductDiscountValue(entry.product);
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

    @Override
    public Ticket<ProductType> clone() {
        return new Ticket<>(this);
    }

    /**
     * Shows ticket id, with product details and price summary
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Ticket: ").append(this.ID).append("\n");

        if (this.entries.isEmpty()) {
            sb.append("No products added yet.\n");
        } else {
            for (TicketEntry<ProductType> entry : this.entries.values()) {
                ProductType product = entry.product;
                String discountSuffix = null;

                if (entry instanceof ProductEntry) {
                    double unitDiscount = getProductDiscountValue(product);
                    if (unitDiscount > 0) {
                        discountSuffix = String.format(" **discount -%.2f", unitDiscount);
                    }
                }

                for (int i = 0; i < entry.getProductCount(); i++) {
                    sb.append(entry);
                    if (discountSuffix != null)
                        sb.append(discountSuffix).append("\n");
                }
            }
        }

        sb.append("\n");
        sb.append(String.format("Total price: %.2f €\n", calculateTotalPrice()));
        sb.append(String.format("Total discount: %.2f €\n", calculateTotalDiscount()));
        sb.append(String.format("Final price: %.2f €\n", calculateFinalPrice()));

        return sb.toString();
    }

    private final TicketID ID;
    private TicketState ticketState;
    private final HashMap<ProductID, TicketEntry<ProductType>> entries;
    private final HashMap<Category, Integer> categories;
    private int totalUnits = 0;

    private static final int MAX_PRODUCTS_PER_TICKET = 100;
    private static final int MIN_UNITS_FOR_DISCOUNT = 2;
}