package es.upm.etsisi.poo.Models.Product.Products.Event;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.Models.Product.ProductEnums.EventType;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketClosingConstraint;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;

public class EventEntry extends TicketEntry<EventProduct, EventEntry> implements TicketClosingConstraint {

    private static final long serialVersionUID = 1L;

    private int people = 0;

    public EventEntry(EventProduct event) throws AppException {
        super(event);

        EventType type = event.getEventType();
        int requiredHours = type.getPlanningTime();
        if (event.isNotPossibleToPlanFromNow()) {
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
    public void checkValidity() throws InvalidPeopleInEventException, NotEnoughPlanningForEventException {
        if (product.getMaxPeople() >= people)
            throw new InvalidPeopleInEventException();

        if (product.hasExpired())
            throw new NotEnoughPlanningForEventException(
                    product.getID().toString(),
                    product.getEventType().getPlanningTime(),
                    product.getExpireDate()
            );
    }

    @Override
    public void accumulate(EventEntry more) throws EntityAlreadyExistsException {
        throw new EntityAlreadyExistsException(
                product.getEventType().toString(),
                product.getID().toString(),
                "you can't add an event twice to the same ticket");
    }
}