package es.upm.etsisi.poo.Models.Product.Products;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.ProductEnums.EventType;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;
import es.upm.etsisi.poo.AppExceptions.InvalidPeopleInEventException;
import es.upm.etsisi.poo.Models.Product.Products.EventProduct;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.EventType;

import java.time.LocalDateTime;

public class EventEntry extends TicketEntry<EventProduct> {

    private static final long serialVersionUID = 1L;

    private int people = 0;

    public EventEntry(EventProduct event) throws AppException {
        super(event);

        EventType type = event.getEventType();

        int requiredHours;
        if (type == EventType.FOOD) {
            requiredHours = 72;
        } else if (type == EventType.MEETING) {
            requiredHours = 12;
        } else {
            requiredHours = type.getPlanningTime();
        }

        if (event.getExpireDate().isBefore(LocalDateTime.now().plusHours(requiredHours))) {
            throw new AppException("Event expires in less than " + requiredHours);
        }
    }

    /**
     * Sets the actual people value to the one in the parameter. It checks
     * @param actualPeople the new value of actualPeople
     */
    public void setActualPeople (int actualPeople)
            throws InvalidPeopleInEventException {
        if (actualPeople > product.getMaxPeople() ||  actualPeople < 1)
            throw new InvalidPeopleInEventException();

        people = actualPeople;
    }

    @Override
    public String toString() {

        return "{class:" + product.getEventType()
                + ", id:" + product.getID()
                + ", name:'" + product.getName()
                + "', price/person:" + product.getPrice()
                + ", expiration date:" + product.getExpireDate().toLocalDate().toString()
                + ", max people allowed:" + product.getMaxPeople()
                + ", actual People in Event:" + people
                + "}";
    }

    @Override
    public int getProductCount() {
        return 1;
    }

    @Override
    public double getPrice() {
        return product.getPrice() * people;
    }

    @Override
    public boolean checkValidity() {
        return product.getMaxPeople() >= people;
    }
}