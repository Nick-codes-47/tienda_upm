package es.upm.etsisi.poo.Actions.ProductHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.CustomProduct;
import es.upm.etsisi.poo.ProductContainer.Product;

/**
 * Class to add a Product or CustomProduct to the catalog
 */
public class AddProduct extends Action {
    public AddProduct(App app) {
        super(app);
    }

    /**
     * Method that executes the action to add a Product or CustomProduct to the catalog
     * @param args the arguments required to add a product to the catalog
     * @return -1 if the catalog is full
     *         0 if all went well
     *         1 if the price or maxPersonalizable were not double or int respectively
     *         2 if one of the arguments was invalid to create the product
     *         3 if they weren't enough arguments
     */
    @Override
    public int execute(String[] args) {
        switch (args.length) {
            case 3: {
                // Case of a normal product
                int newId = app.catalog.getNewId();
                try {
                    BaseProduct product = new Product(
                            newId,  // id
                            args[0],    // name
                            args[1],    // category
                            Double.parseDouble(args[2])     // price
                    );
                    return app.catalog.add(product);
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: price is not valid");
                    return 1;
                } catch (BaseProduct.InvalidProductException e) {
                    System.err.println(e.getMessage());
                    return 2;
                }
            }
            case 4: {
                // Case of a personalizable product
                int newId = app.catalog.getNewId();
                try {
                    BaseProduct product = new CustomProduct(
                            newId,  // id
                            args[0],    // name
                            args[1],    // category
                            Double.parseDouble(args[2]),     // price
                            Integer.parseInt(args[3])   // max_Personalizable
                    );
                    return app.catalog.add(product);
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: price and/or maxPers are not valid");
                    return 1;
                } catch (BaseProduct.InvalidProductException e) {
                    System.err.println(e.getMessage());
                    return 2;
                }
            }
            default:  {
                System.err.println("ERROR: wrong number of arguments");
                return 3;
            }
        }
    }

    /**
     * Shows how to call the action
     */
    @Override
    public void help() {
        System.out.println("prod add \"<name>\" <category> <price> [<maxPers>]");
    }
}
