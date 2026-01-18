package es.upm.etsisi.poo.Models.Ticket.Core;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Models.Core.Copyable;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Supplier;

public abstract class Ticket<ProductType extends BaseProduct<?>>
        implements Iterable<TicketEntry<ProductType, ?>>, Copyable<Ticket<ProductType>>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Class<ProductType> productType;

    private final TicketID ID;
    private TicketState ticketState;
    private final HashMap<ProductID, TicketEntry<ProductType,?>> entries;
    private int totalUnits = 0;

    private final Supplier<PrinterStrategy> printStrat;

    private static final int MAX_PRODUCTS_PER_TICKET = 100;

    public Ticket(TicketID ID, Supplier<PrinterStrategy> printerStrat, Class<ProductType> productType) {
        this.ID = ID;
        this.productType = productType;
        this.entries = new HashMap<>();
        this.ticketState = TicketState.VACIO;
        this.printStrat = printerStrat;
    }

    public Ticket(Ticket<ProductType> other) { // TODO shallow copies instead of deep copy
        this(other.ID, other.printStrat, other.productType);
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
    public Iterator<TicketEntry<ProductType,?>> iterator() {
        return entries.values().iterator();
    }

    private void checkCapacity(int added) throws FullContainerException {
        if ((totalUnits + added) > MAX_PRODUCTS_PER_TICKET) throw new FullContainerException();
    }

    @SuppressWarnings("unchecked")
    private <Entry extends TicketEntry<ProductType, Entry>>
    void accumulateSafe(
            TicketEntry<ProductType, ?> oldEntry,
            TicketEntry<ProductType, ?> newEntry) throws EntityAlreadyExistsException {

        // We cast both to 'E'. We know this is safe because their ProductIDs matched.
        Entry specificOld = (Entry) oldEntry;
        Entry specificNew = (Entry) newEntry;

        specificOld.accumulate(specificNew);
    }

    protected int add(TicketEntry<ProductType, ?> entry)
            throws ClosedTicketException, FullContainerException, EntityAlreadyExistsException {
        if (this.ticketState == TicketState.CERRADO) throw new ClosedTicketException(this.ID.toString());

        if (this.ticketState == TicketState.VACIO) {
            this.ticketState = TicketState.ACTIVO;
        }

        checkCapacity(entry.getProductCount());

        ProductID entryID = entry.getProduct().getID();
        TicketEntry<ProductType, ?> oldEntry = entries.get(entryID);
        if (oldEntry != null) {
            accumulateSafe(oldEntry, entry);
        } else {
            entries.put(entryID, entry);
        }

        totalUnits += entry.getProductCount();

        AppLogger.info(this.toString());
        return 0;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseProduct<?>>
    int add(T baseProd, String[] args) throws AppException, ClassCastException {
        ProductType prod = productType.cast(baseProd);
        return add((TicketEntry<ProductType, ?>) prod.toTicketEntry(args));
    }

    public void delete(ProductID ID)
            throws AppEntityNotFoundException, ClosedTicketException {
        if (this.ticketState == TicketState.CERRADO) throw new ClosedTicketException(this.ID.toString());

        TicketEntry<?,?> entry = entries.get(ID);
        if (entry == null) throw new AppEntityNotFoundException("product", ID.toString());

        entries.remove(ID);
    }

    @SuppressWarnings("unchecked") // We checked the cast by comparing IDs
    public void update(BaseProduct<?> baseProduct) throws AppException {
        if (this.ticketState == TicketState.CERRADO) throw new ClosedTicketException(this.ID.toString());

        TicketEntry<ProductType,?> entry = this.entries.get(baseProduct.getID());

        if (entry != null) {
            ProductType product = (ProductType) baseProduct;
            entry.update(product);
        }
    }

    public void close() throws AppException {
        if (this.ticketState == TicketState.VACIO) {
            throw new AppException("an empty ticket can not be closed");
        }

        if (this.ticketState != TicketState.CERRADO) {
            checkForClosingConstraints();

            this.ticketState = TicketState.CERRADO;
            ID.close();
        }
    }

    private void checkForClosingConstraints() throws AppException {
        if (this instanceof TicketClosingConstraint constraintTicket) {
            constraintTicket.checkValidity();
        }

        for (TicketEntry<ProductType, ?> entry : entries.values()) {
            if (entry instanceof TicketClosingConstraint constraintEntry)
                constraintEntry.checkValidity();
        }
    }

    public void print() throws AppException {
        StringBuilder str = new StringBuilder();

        close();

        PrinterStrategy printer = printStrat.get();
        printer.init(this);
        for (TicketEntry<ProductType, ?> entry : entries.values()) {
            str.append(printer.printEntry(entry));
        }

        str.append(printer.printFooter());
        AppLogger.info(str.toString());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (TicketEntry<ProductType, ?> entry : this) {
            str.append(entry);
        }

        return str.toString();
    }
}