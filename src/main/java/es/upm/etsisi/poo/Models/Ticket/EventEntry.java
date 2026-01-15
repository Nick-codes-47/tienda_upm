package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Core.AppException;
import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Product.Products.EventProduct;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.EventType;

import java.time.LocalDateTime;

public class EventEntry extends TicketEntry<EventProduct> {

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
     * @throws GoodsProduct.InvalidProductException Controls that the parameter actualPeople is valid in the Event
     */
    public void setActualPeople (int actualPeople) throws AppException {
        if (actualPeople > product.getMaxPeople()) {
            throw new AppException(
                    "ERROR: There can't be more than "
                            + product.getMaxPeople() + " people"
            );
        } else if (actualPeople < 1) {
            throw new AppException(
                    "ERROR: There must be at least 1 person in the event"
            );
        }
        people = actualPeople;
    }

    @Override
    public String toString() {

        return "{class:" + EventType.toSentenceCase(product.getEventType())
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

    private int people = 0;
}
