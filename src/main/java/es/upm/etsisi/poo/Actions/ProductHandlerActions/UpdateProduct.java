package es.upm.etsisi.poo.Actions.ProductHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.Category;
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
            BaseProduct product = (BaseProduct) app.catalog.getProduct(id);
            if (product == null) {
                // Product does not exist
                System.err.println("ERROR: Product with id " + id + " does not exist!");
                return 4;
            }

            // We obtain the field to modify and the new value
            String fieldName = args[1].toLowerCase();
            String newValue = args[2];

            String[] supportedFields = {"name","price","category"};
            int i = 0;
            while (i < supportedFields.length && supportedFields[i].equals(fieldName)) {
                i++;
            }
            if (i >= supportedFields.length) {
                System.err.println("ERROR: field " + fieldName + " not found!");
                return 5;
            }

            // We look in the products class and its superclasses to get the field if it exists
            Field field = getFieldFromHierarchy(product.getClass(), fieldName);
            field.setAccessible(true);

            // We convert
            Object converted = convertValue(field, newValue);
            field.set(product, converted);

            // We print the product updated
            System.out.println(product);

            // We look for tickets that had this product
            ArrayList<Ticket> tickets = app.tickets.getTicketsWithProd(product);
            // We show only the tickets that are open
            boolean anyOpen = false;
            for (Ticket ticket : tickets) {
                if (!ticket.isClosed()) {
                    if (!anyOpen) {
                        System.out.println("The tickets with the following ids had the product and it was updated:");
                        anyOpen = true;
                    }
                    System.out.println("- " + ticket.getTicketId());
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
            System.out.println(Category.getCategories());
            return 8;
        }
    }

    /**
     * Shows how to call the action to update a product
     *
     * @return a string with the command and its arguments
     */
    @Override
    public String help() {
        return "prod update <id> NAME|CATEGORY|PRICE <value>";
    }

    private Field getFieldFromHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        // We get the current class of the object
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // if this class doesn't have this field we look in the super class
                current = current.getSuperclass();
            }
        }
        // If we didn't find the field we let it know with a new NoSuchFieldException
        throw new NoSuchFieldException("Field '" + fieldName + "' not found");
    }

    private Object convertValue(Field field, String value) {
        // We get the class of the field to be modified
        Class<?> type = field.getType();

        // We see which type is the field and return the value parsed
        if (type.equals(String.class)) {
            return value;
        }
        if (type.equals(int.class) || type.equals(Integer.class)) {
            return Integer.parseInt(value);
        }
        if (type.equals(double.class) || type.equals(Double.class)) {
            return Double.parseDouble(value);
        }

        // if we didn't manage to parse to one of the permitted classes we throw a new IllegalArgumentException
        throw new IllegalArgumentException("Unsupported field type: " + type.getName());
    }
}
