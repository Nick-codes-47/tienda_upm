package es.upm.etsisi.poo.Commands.Product;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.AppLogger;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.ProductEnums.Category;
import es.upm.etsisi.poo.Services.TicketService;

import java.lang.reflect.Field;

public class UpdateProduct implements Command {
    public static final String ID = "update";

    public UpdateProduct(Catalog catalog, TicketService ticketService) {
        this.catalog = catalog;
        this.ticketService = ticketService;
    }

    /**
     * Method to update the fields of a product (NAME, CATEGORY, PRICE)
     * @param args the parameters to update the product
     * @return 0 if it was successful
     * 1 if one of the number arguments is invalid
     * 3 if the number of arguments is wrong
     * 4 if the product is not in the catalog
     * 5 if the field is invalid
     * 6 if the user doesn't have access to change the specified field
     * 8 if the category introduced is invalid
     */
    @Override
    public int execute(String[] args) throws AppException {
        if (args.length != 3) {
            throw new WrongNumberOfArgsException();
        }
        // if the number of arguments are correct we try to update
        ProductID ID = getProductID(args);
        BaseProduct<?> product = catalog.get(ID);
        if (product == null) throw new AppEntityNotFoundException("product", ID.toString());

        // We obtain the field to modify and the new value
        String fieldName = args[1].toLowerCase();
        String newValue = args[2];

        // We look in the products class and its superclasses to get the field if it exists
        Field field = getFieldFromHierarchy(product.getClass(), fieldName);
        field.setAccessible(true);

        Object converted = converNewtValue(field, newValue);

        setNewValue(field, product, converted);

        AppLogger.info(product.toString());

        ticketService.showModifiedTickets(product);

        return 0;
    }

    private static ProductID getProductID(String[] args)
            throws InvalidAppIDException, NonPositiveNumberException {
        ProductID ID;
        try {
            ID = new ProductID(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            throw new NonPositiveNumberException("ID");
        }
        return ID;
    }

    private Field getFieldFromHierarchy(Class<?> clazz, String fieldName)
            throws FieldNotValidException {
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
        // If we didn't find the field we let it know with a new FieldNotValidException
        throw new FieldNotValidException();
    }

    private Object converNewtValue(Field field, String value)
            throws InvalidNewValueException {
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
        if (type.equals(Category.class)) {
            return Category.valueOf(value);
        }

        // if we didn't manage to parse to one of the permitted classes it's an invalid new value.
        throw new InvalidNewValueException(field.getName());
    }

    private static void setNewValue(Field field, BaseProduct<?> product, Object converted) throws FieldNotValidException {
        try {
            field.set(product, converted);
        } catch (IllegalAccessException e) {
            throw new FieldNotValidException();
        }
    }

    /**
     * Shows how to call the action to update a product
     *
     * @return a string with the command and its arguments
     */
    @Override
    public String help() {
        return ID + " <id> NAME|CATEGORY|PRICE <value>";
    }

    private static class InvalidNewValueException extends AppException {
        private InvalidNewValueException(String field) {
            super("New value is not valid for " + field);
        }
    }

    final private Catalog catalog;
    final private TicketService ticketService;
}