package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Products.Service.ServiceProduct;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntryOrderConstraint;

import java.io.Serializable;
import java.util.Comparator;

public class CombinedPrinter extends CommonPrinter
        implements TicketEntryOrderConstraint, Serializable {

    private static final long serialVersionUID = 1L;

    private int serviceCount = 0;
    private double discountFromServices = 0;

    @Override
    public void init(Ticket<?> ticket) {
        super.init(ticket);

        for (TicketEntry<?, ?> entry : ticket) {
            if (entry.getProduct() instanceof ServiceProduct)
                serviceCount++;
        }
    }

    @Override
    public String printEntry(TicketEntry<?, ?> entry) {
        StringBuilder str = new StringBuilder();

        if (entry.getProduct() instanceof ServiceProduct service)
            str.append(entry);
        else
            str.append(super.printEntry(entry));

        return str.toString();
    }

    @Override
    public String printFooter() {

        return "Total price: " + String.format("%.1f", totalPrice) +
                "\nExtra Discount from services:" + String.format("%.1f", discountFromServices) +
                "\nTotal discount: " + String.format("%.1f **discount -%.1f", discountFromServices, discountFromServices) +
                "\nFinal price: " + String.format("%.1f", totalPrice - totalDiscount - discountFromServices);
    }

    @Override
    public Comparator<TicketEntry<?, ?>> getSortFunction() {
        return SORT_COMPARATOR;
    }

    private final Comparator<TicketEntry<?,?>> SORT_COMPARATOR = Comparator.comparing(
            (e) -> e.getProduct(),
            (p1, p2) -> {
                        if (p1 instanceof ServiceProduct) return 1;
                        else return -1;
                    });
}
