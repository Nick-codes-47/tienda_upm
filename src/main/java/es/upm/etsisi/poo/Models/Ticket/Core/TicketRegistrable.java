package es.upm.etsisi.poo.Models.Ticket.Core;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;

public interface TicketRegistrable<ProductType extends BaseProduct<?>> {

    TicketEntry<ProductType> toTicketEntry(String[] args) throws AppException;
}
