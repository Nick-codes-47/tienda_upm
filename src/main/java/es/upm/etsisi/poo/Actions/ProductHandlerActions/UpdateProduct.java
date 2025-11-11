package es.upm.etsisi.poo.Actions.ProductHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.Category;
import es.upm.etsisi.poo.ProductContainer.Product;
import es.upm.etsisi.poo.TicketContainer.Ticket;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

public class UpdateProduct extends Action {
    public UpdateProduct(App app) {
        super(app);
    }

    /**
     * Method to update the fields of a product (NAME, CATEGORY, PRICE)
     * @param args the parameters to update the product
     * @return 0 if it was successful
     *         1 if one of the number arguments is invalid
     *         3 if the number of arguments is wrong
     *         4 if the product is not in the catalog
     *         5 if the field is invalid
     *         6 if the user doesn't have access to change the specified field
     *         8 if the category introduced is invalid
     */
    @Override
    public int execute(String[] args) {
        if (args.length != 3) {
            System.err.println("ERROR: wrong number of arguments");
            return 3;
        }
        // if the number of arguments are correct we try to update
        try {
            int id = Integer.parseInt(args[0]);
            BaseProduct product = app.catalog.getProduct(id);
            if (product == null) {
                // Product does not exist
                System.err.println("ERROR: Product with id " + id + " does not exist!");
                return 4;
            }

            // We obtain the field to modify and the new value
            String field = args[1];
            String value = args[2];

            // We only allow to modify the following fields
            Set<String> allowedFields = Set.of("name", "price", "category");

            if (!allowedFields.contains(field.toLowerCase())) {
                System.err.println("ERROR: You can only modify 'NAME', 'PRICE' or 'CATEGORY'");
                return 1;
            }

            // We try to modify the field once we know it's valid
            Field f = product.getClass().getDeclaredField(field); // TODO be careful if Products variables are written in camelCase
            f.setAccessible(true);

            if (f.getType().equals(double.class) || f.getType().equals(Double.class)) {
                // The price is modified
                f.set(product, Double.parseDouble(value));
            } else if (f.getType().equals(Category.class)) {
                // The category is modified
                f.set(product, Category.valueOf(value));
            } else {
                // The name is modified
                f.set(product, value);
            }

            // We print the product updated
            System.out.println(product);

            // We look for tickets that had this product
            ArrayList<Ticket> tickets = app.tickets.getTicketsWithProd(id);
            // We show only the tickets that are open
            boolean anyOpen = false;
            for (Ticket ticket : tickets) {
                if (!ticket.isClosed()) {
                    if (!anyOpen) {
                        System.out.println("The tickets with the following ids had the product and it was updated:");
                        anyOpen = true;
                    }
                    System.out.println("- " + ticket.getId());
                }
            }

            return 0;

        } catch (NoSuchFieldException e) {
            System.err.println("ERROR: Field not valid for this product! (Events don't have category)");
            return 5;
        } catch (IllegalAccessException e) {
            System.err.println("ERROR: Illegal access! (You can't modify that field)");
            return 6;
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Id or price is not valid");
            return 1;
        } catch (IllegalArgumentException e) {
            // The value of the new category is not valid
            System.err.println("ERROR category is not valid");

            System.out.println("Valid categories:");
            boolean coma = false;
            for (Category category : Category.values()) {
                if (!coma) {
                    System.out.println(category.name());
                    coma = true;
                }
                else System.out.println(", "+category.name());
            }
            return 8;
        }
    }

    /**
     * Shows how to call the action to update a product
     */
    @Override
    public void help() {
        System.out.println("prod update <id> NAME|CATEGORY|PRICE <value>");
    }
}
