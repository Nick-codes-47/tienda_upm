package es.upm.etsisi.poo.Actions.ProductHandlerActions.Add;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.CustomProduct;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.Product;

/**
 * Class to add a Product or CustomProduct to the catalog
 */
public class AddProduct implements Action, SupportMethods {
    public static final String ID = "add";

    public AddProduct() {
    }

    /**
     * Method that executes the action to add a Product or CustomProduct to the catalog
     * @param args the arguments required to add a product to the catalog
     * @return -3 if the id is already in the catalog
     *         -1 if the catalog is full
     *         0 if all went well
     *         1 if id, price or maxPersonalizable were not valid
     *         2 if one of the arguments was invalid to create the product
     *         3 if they weren't enough arguments
     */
    @Override
    public int execute(String[] args) {

        // We obtain the final id depending on whether the user passed the id or not
        ParsedIdResult parsed = parseOptionalId(args);
        int finalId = parsed.id;
        int offset = parsed.offset; // used to get the right arguments in the constructor

        // We need 3 or 4 real arguments:
        // 3 → normal product
        // 4 → customizable product
        int realArgs = args.length - offset;
        if (realArgs != 3 && realArgs != 4) {
            System.err.println("ERROR: wrong number of arguments");
            return 3;
        }

        try {
            BaseProduct product;

            if (realArgs == 3) {
                // Normal product
                product = new Product(
                        finalId,                                   // id
                        args[offset],                              // name
                        args[offset + 1],                          // category
                        Double.parseDouble(args[offset + 2])        // price
                );

            } else {
                // Customizable product (4 args)
                product = new CustomProduct(
                        finalId,                                   // id
                        args[offset],                              // name
                        args[offset + 1],                          // category
                        Double.parseDouble(args[offset + 2]),       // price
                        Integer.parseInt(args[offset + 3])          // maxPersonalizable
                );
            }

            return addToCatalog(product);

        } catch (NumberFormatException e) {
            System.err.println("ERROR: price and/or maxPers are not valid. " +
                    "(Please, Do not write just a number as a product's name)");
            return 1;
        } catch (BaseProduct.InvalidProductException e) {
            System.err.println(e.getMessage());
            return 2;
        }
    }


    /**
     * Shows how to call the action
     *
     * @return a string with the command and its arguments
     */
    @Override
    public String help() {
        return ID + " [id] \"<name>\" <category> <price> [<maxPers>]";
    }
}
