package es.upm.etsisi.poo.Models.Product.Products.Event;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.NotEnoughPlanningForEventException;
import es.upm.etsisi.poo.Models.Product.ProductEnums.EventType;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;
import es.upm.etsisi.poo.AppExceptions.InvalidPeopleInEventException;

import java.time.LocalDateTime;

public class EventEntry extends TicketEntry<EventProduct> {

    private static final long serialVersionUID = 1L;

    private int people = 0;

    public EventEntry(EventProduct event) throws AppException {
        super(event);

        EventType type = event.getEventType();

        int requiredHours = type.getPlanningTime();

        if (event.isNotPossibleToPlanFromNow(LocalDateTime.now())) {
            throw new NotEnoughPlanningForEventException(event.getID().toString(), requiredHours, event.getExpireDate());
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