package es.upm.etsisi.poo.Actions.ProductHandlerActions.Add;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.BaseProduct;

public interface SupportMethods {
    /**
     * Parses an optional ID from the first argument.
     * If the first argument is an integer, it is treated as the product ID.
     * Otherwise, a new ID is generated.
     *
     * @param args the arguments passed to the action
     * @param app the application object
     * @return a ParsedIdResult containing the ID, the argument offset, and
     *         whether the ID was provided manually
     */
    default ParsedIdResult parseOptionalId(String[] args, App app) {

        boolean idProvided;
        int id = -1;

        // Try to parse the first argument as an ID
        try {
            id = Integer.parseInt(args[0]);
            idProvided = true;
        } catch (NumberFormatException e) {
            idProvided = false;
        }

        // If an ID is provided, shift the parameters by one
        int offset = idProvided ? 1 : 0;

        // If the ID was not provided, generate a new one
        int finalId = idProvided ? id : app.catalog.getNewId();

        return new ParsedIdResult(finalId, offset);
    }

    /**
     * A small container class storing the result of parsing an optional ID.
     */
    class ParsedIdResult {
        public final int id;
        public final int offset;

        public ParsedIdResult(int id, int offset) {
            this.id = id;
            this.offset = offset;
        }
    }

    /**
     * Method to handle the adding of a product to the catalog and the possible errors
     * @param app The app to access the catalog
     * @param product The product to be added
     * @return 0 if all went well
     *         -1 if the product is null
     *         -2 if the catalog is full
     *         -3 if the product's id already exists
     */
    default int addToCatalog(App app, BaseProduct product) {
        int add = app.catalog.add(product);
        if (add == 0){
            // If the product was added we show it
            System.out.println(product);
        }
        else if (add == -3) System.out.println("ERROR: There's already a product with this id");
        else if (add == -2) System.err.println("ERROR: Product is null");
        else if (add == -1) System.err.println("ERROR: You reached the maximum number of products!");
        return add;
    }
}
