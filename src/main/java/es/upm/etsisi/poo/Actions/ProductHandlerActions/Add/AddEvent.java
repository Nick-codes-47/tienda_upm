package es.upm.etsisi.poo.Actions.ProductHandlerActions.Add;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.Event;
import es.upm.etsisi.poo.ProductContainer.EventType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Class to add an Event to the Catalog
 */
public class AddEvent extends Action {
    public AddEvent(App app, EventType eventType) {
        super(app);
        this.eventType = eventType;
    }

    /**
     * Method that executes the action to add an event to the catalog
     * @param args the arguments required to add an event to the catalog
     * @return -1 if the catalog is full
     *         0 if all went well
     *         1 if an argument was invalid to parse
     *         2 if one of the arguments was invalid to create the product
     *         3 if they weren't enough arguments
     */
    @Override
    public int execute(String[] args) {
        if (args.length == 4) {
            int newId = app.catalog.getNewId();
            try {
                BaseProduct event = new Event(
                        newId,                                          // ID
                        args[0],                                        // name
                        Double.parseDouble(args[1]),                    // price
                        LocalDate.parse(args[2]).atStartOfDay(),        // expiration date
                        Integer.parseInt(args[3]),                      // max people
                        this.eventType                                  // type
                );
                return AddProduct.addToCatalog(app, event);
            } catch (NumberFormatException e) {
                System.err.println("ERROR: price and/or max_people are not valid");
                return 1;
            } catch (DateTimeParseException e) {
                System.err.println("ERROR: the date MUST have the format: yyyy-MM-dd");
                return 1;
            } catch (BaseProduct.InvalidProductException e) {
                System.err.println(e.getMessage());
                return 2;
            }
        } else {
            System.err.println("ERROR: wrong number of arguments");
            return 3;
        }
    }

    /**
     * Shows how to call all actions that involve adding an event
     */
    @Override
    public void help() {
        System.out.println("add" + EventType.toSentenceCase(eventType) +
                " \"<name>\" <price> <expiration: yyyy-MM-dd> <max_people>");
    }

    private final EventType eventType;
}
