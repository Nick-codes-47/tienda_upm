package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Products.Service.ServiceProduct;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntryOrderConstraint;

import java.io.Serializable;
import java.util.Comparator;

public class CombinedPrinter extends CommonPrinter
        implements TicketEntryOrderConstraint, Serializable {

    private static final long serialVersionUID = 1L;

    private static double DISCOUNT_PER_SERVICE = 0.15f;

    private double discountFromServices = 0;

    @Override
    public void init(Ticket<?> ticket) {
        super.init(ticket);

        for (TicketEntry<?, ?> entry : ticket) {
            if (entry.getProduct() instanceof ServiceProduct)
                if (discountFromServices < 1.f)
                    discountFromServices += DISCOUNT_PER_SERVICE;
        }
    }


    @Override
    public String printHeader() {
        return "Services Included\n";
    }

    @Override
    public String printEntry(TicketEntry<?, ?> entry) {
        StringBuilder str = new StringBuilder();

        if (entry.getProduct() instanceof ServiceProduct service)
            str.append(entry).append("\n");
        else {
            if (totalPrice == 0) str.append("Product Included\n");
            str.append(super.printEntry(entry));
        }
        return str.toString();
    }

    @Override
    public String printFooter() {
        double serviceDiscount = this.totalPrice * discountFromServices;
        double finalPrice = this.totalPrice - serviceDiscount;
        this.totalDiscount += serviceDiscount;

        return "Total price: " + String.format("%.1f", totalPrice) +
                "\nExtra Discount from services:" + String.format("%.1f  **discount -%.1f", serviceDiscount, serviceDiscount) +
                "\nTotal discount: " + String.format("%.1f", totalDiscount) +
                "\nFinal price: " + String.format("%.1f", finalPrice);
    }

    @Override
    public Comparator<TicketEntry<?, ?>> getSortFunction() {
        return SORT_COMPARATOR;
    }

    private final Comparator<TicketEntry<?,?>> SORT_COMPARATOR = Comparator.comparing(
            TicketEntry::getProduct,
            (p1, p2) -> {
                        if (p1 instanceof ServiceProduct) return -1;
                        else if (p2 instanceof ServiceProduct) return 1;
                        else return 0;
                    });
}
