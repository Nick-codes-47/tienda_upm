package es.upm.etsisi.poo.Models.Ticket.Core;

import es.upm.etsisi.poo.Models.Product.Products.ServiceProduct;

public class ServiceEntry extends TicketEntry<ServiceProduct> {

    private static final long serialVersionUID = 1L;

    public ServiceEntry(ServiceProduct service) {
        super(service);
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public int getProductCount() {
        return 1;
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public boolean checkValidity() {
        return true;
    }
}