package es.upm.etsisi.poo.Models.Ticket.Core;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Models.Core.Copyable;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.Event.EventProduct;
import es.upm.etsisi.poo.Models.Product.Products.Service.ServiceProduct;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class Ticket<ProductType extends BaseProduct<?>>
        implements Printable, Iterable<TicketEntry<ProductType>>, Copyable<Ticket<ProductType>>, Serializable {

    private static final long serialVersionUID = 1L;


    private final TicketID ID;
    private TicketState ticketState;
    private final HashMap<ProductID, TicketEntry<ProductType>> entries;
    private int totalUnits = 0;

    private static final int MAX_PRODUCTS_PER_TICKET = 100;

    public Ticket(TicketID ID) {
        this.ID = ID;
        this.entries = new HashMap<>();
        this.ticketState = TicketState.VACIO;
    }

    public Ticket(Ticket<ProductType> other) { // TODO shallow copies instead of deep copy
        this(other.ID);
        this.ticketState = other.ticketState;
        this.entries.putAll(other.entries);
        this.totalUnits = other.totalUnits;
    }

    public TicketID getID() { return ID; }
    public TicketState getTicketState() { return this.ticketState; }

    public boolean hasProduct(ProductID productId) {
        return this.entries.containsKey(productId);
    }

    @Override
    public Iterator<TicketEntry<ProductType>> iterator() {
        return entries.values().iterator();
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

        AppLogger.info(this.toString());
        return 0;
    }

    public int add(TicketRegistrable<ProductType> product, String[] args) throws AppException {
        return add(product.toTicketEntry(args));
    }

    public void delete(ProductID ID)
            throws AppEntityNotFoundException, ClosedTicketException {
        if (this.ticketState == TicketState.CERRADO) throw new ClosedTicketException(this.ID.toString());

        TicketEntry<?> entry = entries.get(ID);
        if (entry == null) throw new AppEntityNotFoundException("product", ID.toString());

        entries.remove(ID);
    }

    @SuppressWarnings("unchecked") // We checked the cast by comparing IDs
    public void update(BaseProduct<?> baseProduct) throws AppException {
        if (this.ticketState == TicketState.CERRADO) throw new ClosedTicketException(this.ID.toString());

        TicketEntry<ProductType> entry = this.entries.get(baseProduct.getID());

        if (entry != null) {
            ProductType product = (ProductType) baseProduct;
            entry.update(product);
        }
    }

    public void close() throws ExpiredException {
        ArrayList<EventProduct> eventsInTicket = getProductsOfTypeFromTicket(EventProduct.class);
        checkForNonProgrammableEvents(eventsInTicket);

        ArrayList<ServiceProduct> servicesInTicket = getProductsOfTypeFromTicket(ServiceProduct.class);
        checkForExpiredServices(servicesInTicket);

        if (this.ticketState != TicketState.CERRADO) {
            this.ticketState = TicketState.CERRADO;
            ID.close();
        }
    }

    private <T extends BaseProduct<?>> ArrayList<T> getProductsOfTypeFromTicket(Class<T> productType) {
        ArrayList<T> products = new ArrayList<>();

        for (TicketEntry<ProductType> entry : entries.values()) {
            BaseProduct<?> product = entry.getProduct();

            if (productType.isInstance(product)) {
                products.add(productType.cast(product));
            }
        }

        return products;
    }

    // TODO implement an expirable interface with an isExpired method refactor these methods into one
    private static void checkForNonProgrammableEvents(ArrayList<EventProduct> eventsInTicket) throws NotEnoughPlanningForEventException {
        for (EventProduct event : eventsInTicket) {
            if (event.isNotPossibleToPlanFromNow(LocalDateTime.now())) {
                throw new NotEnoughPlanningForEventException
                        (event.getID().toString(), event.getEventType().getPlanningTime(),event.getExpireDate());
            }
        }
    }

    private static void checkForExpiredServices(ArrayList<ServiceProduct> servicesInTicket) throws ExpiredServiceException {
        for (ServiceProduct service : servicesInTicket) {
            if (service.hasExpired()) {
                throw new ExpiredServiceException(service.getID().toString());
            }
        }
    }

    private boolean isEvent(TicketEntry<ProductType> entry) {
        return entry.getProduct() instanceof EventProduct;
    }
}