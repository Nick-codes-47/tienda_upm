package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.ProductEnums.Category;
import es.upm.etsisi.poo.Models.Product.Products.Product.Product;
import es.upm.etsisi.poo.Models.Ticket.Core.PrinterStrategy;
import es.upm.etsisi.poo.Models.Ticket.Core.Ticket;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketEntry;

import java.io.Serializable;
import java.util.HashMap;

public class CommonPrinter implements PrinterStrategy, Serializable {


    private static int MIN_AMOUNT_FOR_DISCOUNT = 2;

    HashMap<Category, Integer> categoryCount = new HashMap<>();
    double totalPrice = 0;
    double totalDiscount = 0;

    public CommonPrinter() {};

    @Override
    public void init(Ticket<?> ticket) {
        for (TicketEntry<?, ?> entry : ticket) {
            if (entry.getProduct() instanceof Product product) {
                Category category = product.getCategory();
                int currentQuantity = categoryCount.getOrDefault(category, 0);
                categoryCount.put(category, currentQuantity + entry.getProductCount());
            }
        }
    }

    @Override
    public String printEntry(TicketEntry<?, ?> entry) {
        StringBuilder str = new StringBuilder();

        if (entry.getProduct() instanceof Product product) {
            Category category = product.getCategory();
            double discountRate = category.getDiscount() / 100;

            for (int i = 0; i < entry.getProductCount(); i++) {
                str.append(entry);
                if (categoryCount.get(category) >= MIN_AMOUNT_FOR_DISCOUNT) {
                    double discount = product.getPrice() * discountRate;
                    if (discount > 0) {
                        str.append(" **discount -").append(String.format("%.1f\n", discount));
                        totalDiscount += discount;
                    }
                }
                totalPrice += product.getPrice();
            }
        }

        return str.toString();
    }

    @Override
    public String printFooter() {

        return "Total price: " + String.format("%.1f", totalPrice) +
                "\nTotal discount: " + String.format("%.1f", totalDiscount) +
                "\nFinal price: " + String.format("%.1f", totalPrice - totalDiscount);
    }
}
