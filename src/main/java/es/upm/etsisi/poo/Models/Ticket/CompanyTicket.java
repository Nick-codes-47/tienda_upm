package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Products.Product.Product;
import es.upm.etsisi.poo.Models.Product.Products.Service.ServiceProduct;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketClosingConstraint;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketID;

import java.io.Serializable;

public class CompanyTicket extends Ticket<BaseProduct<?>> implements TicketClosingConstraint, Serializable {

    private static final long serialVersionUID = 1L;

    public CompanyTicket(TicketID ID) {
        super(ID, CombinedPrinter::new, (Class) BaseProduct.class);
    }

    public CompanyTicket(CompanyTicket other) {
        super(other);
    }

    @Override
    public CompanyTicket copy() {
        return new CompanyTicket(this);
    }

    @Override
    public void checkValidity() throws AppException {
        boolean hasService = false, hasProduct = false;

        for (TicketEntry<BaseProduct<?>, ?> entry : this) {
            if (entry.getProduct() instanceof ServiceProduct)
                hasService = true;
            else if (entry.getProduct() instanceof Product)
                hasProduct = true;
        }

        if (!(hasService && hasProduct))
            throw new AppException("combined tickets must have at least one product and one service");
    }

    @Override
    protected void reloadPrinterStrategy() {
        this.printStrat = CombinedPrinter::new;
    }
}