package es.upm.etsisi.poo.Commands.Product.Add;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Products.*;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Core.ProductName;
import es.upm.etsisi.poo.Models.Product.ProductEnums.EventType;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Class to add an Event to the Catalog
 */
public abstract class AddEvent extends AddProduct {
    public final String ID;

    public AddEvent(Catalog catalog, EventType eventType, String id) {
        super(catalog);

        this.eventType = eventType;
        ID = id;
    }

    @Override
    protected BaseProduct<?> createProduct(String[] args) throws AppException {
        BaseProduct<?> product = null;

        try {
            int rawID = -1;

            if (Character.isDigit(args[0].charAt(0))) // only IDs start with numbers
                rawID = Integer.parseInt(args[0]);

            if (args.length == 4 && rawID == -1) {
                ProductID ID = catalog.getNewProductID();
                product = new EventProduct(eventType, ID, new ProductName(args[0]),
                        Double.parseDouble(args[1]), LocalDateTime.parse(args[2]), Integer.parseInt(args[3]));
            } else if (args.length == 5 && rawID != -1) {
                ProductID ID = new ProductID(rawID);
                product = new EventProduct(eventType, ID, new ProductName(args[1]),
                        Double.parseDouble(args[2]), LocalDateTime.parse(args[3]), Integer.parseInt(args[4]));
            } else
                throw new WrongNumberOfArgsException();

            return product;
        } catch (DateTimeParseException e) {
            System.err.println("ERROR: the date MUST have the format: yyyy-MM-dd");
        }

        return product;
    }

    /**
     * Shows how to call all actions that involve adding an event
     *
     * @return a string with the command and its arguments
     */
    @Override
    public String help() {
        return ID + " [id] \"<name>\" <price> <expiration: yyyy-MM-dd> <max_people>";
    }

    private final EventType eventType;
}
