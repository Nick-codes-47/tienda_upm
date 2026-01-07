package es.upm.etsisi.poo.Services.AddToTicketStrategies;

import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Ticket.Ticket;

public interface AddToTicketStrategy {
    Ticket.Entry add(GoodsProduct product);
}
