package es.upm.etsisi.poo.Actions.ProductHandlerActions.Add;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.Event;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.ProductEnums.EventType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Class to add an Event to the Catalog
 */
public abstract class AddEvent implements Action, SupportMethods {
    public final String ID;

    public AddEvent(EventType eventType, String id) {
        this.eventType = eventType;
        ID = id;
    }

    /**
     * Method that executes the action to add an event to the catalog
     * @param args the arguments required to add an event to the catalog
     * @return -3 if the id is already in the catalog
     *         -1 if the catalog is full
     *         0 if all went well
     *         1 if an argument was invalid
     *         2 if one of the arguments was invalid to create the product
     *         3 if they weren't enough arguments
     */
    @Override
    public int execute(String[] args) {

        // We obtain the final id depending on whether the user passed the id or not
        ParsedIdResult parsed = parseOptionalId(args);
        int finalId = parsed.id;
        int offset = parsed.offset; // used to get the right arguments

        // We need 4 real arguments
        if (args.length - offset != 4) {
            System.err.println("ERROR: wrong number of arguments");
            return 3;
        }

        try {
            BaseProduct event = new Event(
                    finalId,                                        // ID
                    args[offset],                                  // name
                    Double.parseDouble(args[offset + 1]),           // price
                    LocalDate.parse(args[offset + 2]).atStartOfDay(), // expiration date
                    Integer.parseInt(args[offset + 3]),             // max_people
                    this.eventType                                 // event type
            );

            return addToCatalog(event);

        } catch (NumberFormatException e) {
            System.err.println("ERROR: price and/or maxPers are not valid. " +
                    "(Please, Do not write just a number as a product's name)");
            return 1;
        } catch (DateTimeParseException e) {
            System.err.println("ERROR: the date MUST have the format: yyyy-MM-dd");
            return 1;
        } catch (BaseProduct.InvalidProductException e) {
            System.err.println(e.getMessage());
            return 2;
        }
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
