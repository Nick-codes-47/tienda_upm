package es.upm.etsisi.poo.Commands.Product;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.EmptyDataException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;

import java.util.*;

public class ListProducts implements Command {
    public static final String ID = "list";

    public ListProducts(Catalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Method that lists the products in ascending order by id
     * @param args in this action there are no arguments required
     */
    @Override
    public void execute(String[] args) throws AppException {
        if (args.length != 0) throw new WrongNumberOfArgsException(this);

        HashMap<ProductID, BaseProduct<?>> products = catalog.getProducts();
        if (products.isEmpty()) throw new EmptyDataException("products");

        // We list the products in ascending order by their id
        ArrayList<Map.Entry<ProductID, BaseProduct<?>>> entries = new ArrayList<>(products.entrySet());
        entries.sort(APPID_LIST_ORDER);

        StringBuilder productsMessage = new StringBuilder("Catalog:").append("\n");
        for (Map.Entry<ProductID, BaseProduct<?>> entry : entries) {
            productsMessage.append(" ").append(entry.getValue()).append("\n");
        }

        AppLogger.info(productsMessage.toString());
    }

    /**
     * Shows how to call the action to list the products
     *
     * @return a string with the command and its arguments
     */
    @Override
    public String help() {
        return ID;
    }

    private final Catalog catalog;

    private final Comparator<Map.Entry<ProductID, ?>> APPID_LIST_ORDER = Comparator.comparing(
            (Map.Entry<ProductID, ?> e) -> e.getKey().getClass(),
            (class1, class2) -> {
                if (class1 == class2) return 0;
                if (class1 == ProductID.class) return -1;
                else return 1;
            })
            .thenComparing(Map.Entry::getKey, ProductID::compareTo);
}
