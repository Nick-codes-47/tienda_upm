package es.upm.etsisi.poo.ProductContainer;

import java.time.LocalDate;

public class Event extends BaseProduct {

    public Event(int id, String name, double price, LocalDate expireDate, int maxPeople, EventType type)
        throws InvalidProductException{
        super(id,name, price);
        // TODO comprobaciones sobre expireDate y maxPeople
    }

    private LocalDate expireDate;
    private int maxPeople;
    private EventType type;
}
