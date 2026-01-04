package es.upm.etsisi.poo.Commands.Product;

import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;

import java.util.*;

public class ListProducts implements Command {
    public static final String ID = "list";

    public ListProducts(Catalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Method that lists the products in ascending order by id
     * @param args in this action there are no arguments required
     * @return 0 if it was successful
     *         3 if they were arguments
     *         7 if the catalog is empty
     */
    @Override
    public int execute(String[] args) {
        // this action doesn't need arguments (args.length = 0)
        if (args.length != 0) return 3;

        // We obtain the map of products
        HashMap<Integer, BaseProduct> products = catalog.getProducts();
        // We check if the catalog is empty
        if (products.isEmpty()) {
            System.err.println("ERROR: There are no products in the catalog yet!");
            return 7;
        }

        // We list the products in ascending order by their id
        ArrayList<Map.Entry<Integer,BaseProduct>> entries = new ArrayList<>(products.entrySet());
        entries.sort(Map.Entry.comparingByKey());
        System.out.println("Catalog:");
        for (Map.Entry<Integer, BaseProduct> entry : entries) {
            // We only show the product
            System.out.println(" "+entry.getValue());
        }
        return 0;
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
}
