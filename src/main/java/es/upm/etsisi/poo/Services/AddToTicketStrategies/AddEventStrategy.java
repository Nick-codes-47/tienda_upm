package es.upm.etsisi.poo.Services.AddToTicketStrategies;

import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Product.Products.EventProduct;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.EventType;
import es.upm.etsisi.poo.Models.Ticket.Ticket;

import java.time.LocalDateTime;

public class AddEventStrategy implements AddToTicketStrategy {

    @Override
    public Ticket.Entry add(GoodsProduct product) {
        EventProduct event = (EventProduct) product; // TODO can be omitted?

        EventType type = event.getType();

        int requiredHours;
        if (type == EventType.FOOD) {
            requiredHours = 72;
        } else if (type == EventType.MEETING) {
            requiredHours = 12;
        } else {
            requiredHours = type.getPlanningTime();
        }

        LocalDateTime requiredDate = LocalDateTime.now().plusHours(requiredHours);
        LocalDateTime eventDate = event.getExpireDate();

        if (eventDate.isBefore(requiredDate)) {
            // TODO exception
        }
        try {
            event.setActualPeople(quantity);
        } catch (GoodsProduct.InvalidProductException e) {
            // TODO exception
        }

        return new Ticket.Entry(product, quantity);
    }
}
