package es.upm.etsisi.poo.Services.AddToTicketStrategies;

import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Ticket.Ticket;

public class AddProductStrategy implements AddToTicketStrategy {

    @Override
    public Ticket.Entry add(GoodsProduct product, int quantity) {
        return new Ticket.Entry(product, quantity);
    }
}
