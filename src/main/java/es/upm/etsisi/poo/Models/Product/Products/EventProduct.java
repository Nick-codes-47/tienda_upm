package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Core.Copyable;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Core.ProductName;
import es.upm.etsisi.poo.Models.Product.ProductEnums.EventType;
import es.upm.etsisi.poo.AppExceptions.InvalidProductException;
import es.upm.etsisi.poo.Models.Ticket.Core.EntryArgs;

import java.time.LocalDateTime;

public class EventProduct extends GoodsProduct<EventProduct> implements Copyable<EventProduct> {

    private static final long serialVersionUID = 1L;

    private final int maxPeople;
    private final EventType type;
    private final LocalDateTime expireDate;
    private static final int MAX_PEOPLE_ALLOWED = 100;

    public EventProduct(EventType type, ProductID ID, ProductName name, double price, LocalDateTime expireDate, int maxPeople) throws InvalidProductException {
        super(ID, name, price);

        if (maxPeople < 1 || maxPeople > MAX_PEOPLE_ALLOWED)
            throw new InvalidProductException(" events must have between 1 and 100 people");
        if (expireDate == null || expireDate.isBefore(LocalDateTime.now()))
            throw new InvalidProductException("events expireDate can't be null or before now");

        this.type = type;
        this.expireDate = expireDate;
        this.maxPeople = maxPeople;
    }

    public EventProduct(EventProduct other) {
        super(other);

        this.type = other.type;
        this.expireDate = other.expireDate;
        this.maxPeople = other.maxPeople;
    }

    public int getMaxPeople() { return this.maxPeople; }
    public LocalDateTime getExpireDate() { return this.expireDate; }
    public EventType getEventType() { return this.type; }

    @Override
    public EventEntry toTicketEntry(EntryArgs args) throws AppException {
        assert args instanceof EventEntryArgs : "Wrong EntryArgs subclass passed";

        EventEntryArgs eventArgs = (EventEntryArgs) args;
        EventEntry entry = new EventEntry(this);
        entry.setActualPeople(eventArgs.people);
        return entry;
    }

    @Override
    public EventProduct copy() {
        return new EventProduct(this);
    }

    @Override
    public String toString() {

        return "{class:" +
                type +
                ", id:" +
                super.getID() +
                ", name:'" +
                super.getName() +
                "', price/person:" +
                super.getPrice() +
                ", expiration date:" +
                this.expireDate.toLocalDate().toString() +
                ", max people allowed:" +
                this.maxPeople;
    }
}