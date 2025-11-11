package es.upm.etsisi.poo.ProductContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event extends BaseProduct {

    public Event(int id, String name, double price, LocalDateTime expireDate, int maxPeople, EventType type)
        throws InvalidProductException{
        super(id,name, price);
        if (maxPeople < 1 || maxPeople > MAX_PEOPLE_ALLOWED)
            throw new InvalidProductException("ERROR: Events must have between 1 and 100 people");
        if (expireDate == null || expireDate.isBefore(LocalDateTime.now()))
            throw new InvalidProductException("ERROR: ExpireDate can't be null or before now");
        this.expireDate = expireDate;
        this.maxPeople = maxPeople;
        this.type = type;
    }

    public LocalDateTime getExpireDate() { return this.expireDate; }
    public int getMaxPeople() { return this.maxPeople; }
    public EventType getType() { return this.type; }

    @Override
    public String toString() {
        return "{class:"+EventType.toSentenceCase(type)+", id:"+super.getId()+", name:'"+super.getName()+
                ", expiration date:"+this.expireDate.toLocalDate().toString()+", maximum people:"+this.maxPeople+
                ", price/person:" +super.getPrice()+"}";
    }

    private final LocalDateTime expireDate;
    private final int maxPeople;
    private final EventType type;
    private final int MAX_PEOPLE_ALLOWED = 100;
}
