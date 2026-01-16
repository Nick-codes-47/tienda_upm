package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.ClosedTicketException;
import es.upm.etsisi.poo.AppExceptions.FullContainerException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.EventProduct;
import es.upm.etsisi.poo.Models.Product.Products.Product;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.Category;
import es.upm.etsisi.poo.Models.Product.Products.ServiceProduct;
import es.upm.etsisi.poo.Models.Ticket.Core.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Ticket<ProductType extends BaseProduct> implements Serializable {

    private static final long serialVersionUID = 1L;


    private final TicketID ID;
    private final TicketType type;
    private TicketState ticketState;
    private final HashMap<ProductID, TicketEntry<ProductType>> entries;
    private final HashMap<Category, Integer> categories;
    private int totalUnits = 0;

    private static final int MAX_PRODUCTS_PER_TICKET = 100;
    private static final int MIN_UNITS_FOR_DISCOUNT = 2;

    public Ticket(TicketType type, TicketID ID) {
        this.type = type;
        this.ID = ID;
        this.entries = new HashMap<>();
        this.categories = new HashMap<>();
        this.ticketState = TicketState.VACIO;
    }

    public Ticket(Ticket<ProductType> other) { // TODO shallow copies instead of deep copy
        this(other.type, other.ID);
        this.ticketState = other.ticketState;
        this.entries.putAll(other.entries);
        this.categories.putAll(other.categories);
        this.totalUnits = other.totalUnits;
    }

    public TicketType getType() { return type; };
    public TicketID getID() { return ID; }
    public TicketState getTicketState() { return this.ticketState; }

    public boolean hasProduct(ProductID productId) {
        return this.entries.containsKey(productId);
    }

    private void checkCapacity(int added) throws FullContainerException {
        if ((totalUnits + added) > MAX_PRODUCTS_PER_TICKET) throw new FullContainerException();
    }

    private int add(TicketEntry<ProductType> entry)
            throws ClosedTicketException, FullContainerException {
        if (this.ticketState == TicketState.CERRADO) throw new ClosedTicketException(this.ID.toString());

        if (this.ticketState == TicketState.VACIO) {
            this.ticketState = TicketState.ACTIVO;
        }

        checkCapacity(entry.getProductCount());

        entries.put(entry.getProduct().getID(), entry);
        totalUnits += entry.getProductCount();

        if (entry instanceof ProductEntry productEntry) {
            Product product = productEntry.getProduct();
            Category category = product.getCategory();
            int total = categories.getOrDefault(category, 0);
            categories.put(category, total + entry.getProductCount());
        }

        AppLogger.info(this.toString());
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

    public int add(ProductType baseProduct, int amount, ArrayList<String> personalizations) throws AppException {
        TicketEntry<ProductType> entry;
        switch (baseProduct) {
            case Product product:
                entry = (TicketEntry<ProductType>) new ProductEntry(product);
                ((ProductEntry) entry).amount = amount;
                ((ProductEntry) entry).setPersonalizations(personalizations);
                break;
            case null, default:
                throw new AppException("");
        };

        return add(entry);
    }

    public int delete(ProductID ID)
            throws AppEntityNotFoundException, ClosedTicketException {
        if (this.ticketState == TicketState.CERRADO) throw new ClosedTicketException(this.ID.toString());

        TicketEntry<?> entry = entries.get(ID);
        if (entry == null) throw new AppEntityNotFoundException("product", ID.toString());

        if (entry instanceof ProductEntry productEntry) {
            Product product = productEntry.getProduct();
            Category categoryKey = product.getCategory();
            int currentCategoryCount = categories.getOrDefault(categoryKey, 0);
            int newCount = Math.max(0, currentCategoryCount - productEntry.getProductCount());
            categories.put(categoryKey, newCount);
            totalUnits -= productEntry.getProductCount();
        }

        entries.remove(ID);
        return 0;
    }

    public int update(BaseProduct product) throws AppException {
        if (this.ticketState == TicketState.CERRADO) throw new ClosedTicketException(this.ID.toString());

        TicketEntry<ProductType> entry = this.entries.get(product.getID());
        if (entry != null) {
            entry.update((ProductType) product);
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

        AppLogger.info(this.toString());
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (TicketEntry<ProductType> entry : entries.values()) {
            totalPrice += entry.getPrice();
        }
        return totalPrice;
    }

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
            double unitDiscount = getProductDiscountValue(entry.getProduct());
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

        sb.append("Ticket: ").append(this.ID).append("\n");

        if (this.entries.isEmpty()) {
            sb.append("No products added yet.\n");
        } else {
            for (TicketEntry<ProductType> entry : this.entries.values()) {
                ProductType product = entry.getProduct();
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
}