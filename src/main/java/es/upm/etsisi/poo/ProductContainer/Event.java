package es.upm.etsisi.poo.ProductContainer;

import java.time.LocalDate;

public class Event extends BaseProduct {

    public Event(int id, String name, double price, LocalDate expireDate, int maxPeople, EventType type)
        throws InvalidProductException{
        super(id,name, price);
        // TODO comprobaciones sobre planificaciones según el type
        if (maxPeople < 1 || maxPeople > 100)
            throw new InvalidProductException("ERROR: Events must have between 1 and 100 people");
        if (expireDate == null || expireDate.isBefore(LocalDate.now()))
            throw new InvalidProductException("ERROR: ExpireDate can't be null or before now");
        this.expireDate = expireDate;
        this.maxPeople = maxPeople;
        this.type = type;
    }

    public LocalDate getExpireDate() { return this.expireDate; }
    public int getMaxPeople() { return this.maxPeople; }
    public EventType getType() { return this.type; }

    public double getTotalPrice() { return super.getPrice() * this.maxPeople; }

    private LocalDate expireDate;
    private int maxPeople;
    private EventType type;
}
