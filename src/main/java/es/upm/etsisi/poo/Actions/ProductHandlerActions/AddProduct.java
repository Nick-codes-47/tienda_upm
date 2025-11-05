package es.upm.etsisi.poo.Actions.ProductHandlerActions;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.BaseProduct;
import es.upm.etsisi.poo.ProductContainer.Product;

public class AddProduct extends Action {
    public AddProduct(App app) {
        super(app);
    }

    @Override
    public int execute(String[] args) {
        // Case normal product
        if (args.length == 3) {
            // TODO mirar para cambiar la comprobación en función de los enums
            if (app.config.validCategory(args[2])) {
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
                    System.err.println("ERROR: id and/or price are not valid");
                    return 2;
                } catch (BaseProduct.InvalidProductException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        // TODO case personalizable product
        return 0;
    }

    @Override
    public void help() {
        System.out.println("prod add \"<name>\" <category> <price> [<maxPers>]");
    }
}
