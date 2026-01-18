package es.upm.etsisi.poo.Commands.Product.Add;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.AppExceptions.InvalidDateFormatException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Core.ProductName;
import es.upm.etsisi.poo.Models.Product.ProductEnums.EventType;
import es.upm.etsisi.poo.Models.Product.Products.Event.EventProduct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class to add an Event to the Catalog
 */
public abstract class AddEvent extends AddProduct {
    public final String ID;

    private final EventType eventType;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AddEvent(Catalog catalog, EventType eventType, String id) {
        super(catalog);
        this.eventType = eventType;
        this.ID = id;
    }

    @Override
    protected BaseProduct<?> createProduct(String[] args) throws AppException {
        int rawID = -1;

        if (Character.isDigit(args[0].charAt(0))) // only IDs start with numbers
            rawID = Integer.parseInt(args[0]);

        EventToAdd type = resolveEventToAdd(args, rawID);

        return getEvent(args, type, rawID);
    }

    private EventToAdd resolveEventToAdd(String[] args, int rawID) throws WrongNumberOfArgsException {
        return switch (args.length) {
            case 4 -> {
                if (rawID == -1) yield EventToAdd.EVENT_WITHOUT_ID;
                throw new WrongNumberOfArgsException(this);
            }
            case 5 -> {
                if (rawID != -1) yield EventToAdd.EVENT_WITH_ID;
                throw new WrongNumberOfArgsException(this);
            }
            default -> throw new WrongNumberOfArgsException(this);
        };
    }

    private BaseProduct<?> getEvent(String[] args, EventToAdd type, int rawID) throws AppException {
        try {
            return getEventProduct(args, type, rawID);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
    }

    private BaseProduct<?> getEventProduct(String[] args, EventToAdd type, int rawID)
            throws AppException {
        return switch (type) {
            case EVENT_WITHOUT_ID -> {
                ProductID id = catalog.getNewProductID();
                yield new EventProduct(
                        eventType,
                        id,
                        new ProductName(args[0]),
                        Double.parseDouble(args[1]),
                        LocalDate.parse(args[2], DATE_TIME_FORMATTER).atStartOfDay(),
                        Integer.parseInt(args[3])
                );
            }
            case EVENT_WITH_ID -> {
                ProductID id = new ProductID(rawID);
                yield new EventProduct(
                        eventType,
                        id,
                        new ProductName(args[1]),
                        Double.parseDouble(args[2]),
                        LocalDate.parse(args[3], DATE_TIME_FORMATTER).atStartOfDay(),
                        Integer.parseInt(args[4])
                );
            }
        };
    }

    /**
     * Shows how to call all actions that involve adding an event
     */
    @Override
    public String help() {
        return ID + " [id] \"<name>\" <price> <expiration: yyyy-MM-dd> <max_people>";
    }

    private enum EventToAdd {
        EVENT_WITHOUT_ID,
        EVENT_WITH_ID
    }
}
