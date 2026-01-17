package es.upm.etsisi.poo.Commands.Product.Add;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Products.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Products.Core.ProductName;
import es.upm.etsisi.poo.Models.Product.Products.Core.ServiceID;
import es.upm.etsisi.poo.Models.Product.Products.Product;
import es.upm.etsisi.poo.Models.Product.Products.ServiceProduct;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Class to add a Product or CustomProduct to the catalog
 */
public class AddProduct implements Command {
    public static final String ID = "add";

    public AddProduct(Catalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Method that executes the action to add a Product or CustomProduct to the catalog
     * @param args the arguments required to add a product to the catalog
     * @return 0 if all went well
     */
    @Override
    public int execute(String[] args) throws AppException {
        if (args.length < 2) throw new WrongNumberOfArgsException();

        BaseProduct product = createProduct(args);
        catalog.add(product);

        return 0;
    }

    protected BaseProduct createProduct(String[] args) throws AppException {
        BaseProduct product = null;

        int rawID = -1;

        if (Character.isDigit(args[0].charAt(0))) // only IDs start with numbers
            rawID = Integer.parseInt(args[0]);

        // TODO add enum to this class and make this infinite if else a switch
        if (args.length == 2 && rawID == -1) {
            ServiceID ID = catalog.getNewServiceID();
            try {
                product = new ServiceProduct(ID, LocalDateTime.parse(args[0]), args[1]);
            } catch (DateTimeParseException e) {
                // TODO add app exception to handle
            }
        } else if (args.length == 3 && rawID == -1) {
            ProductID ID = catalog.getNewProductID();
            product = new Product(ID, new ProductName(args[0]), args[1], Double.parseDouble(args[2]));
        } else if (args.length == 4 && rawID != -1) {
            ProductID ID = new ProductID(rawID);
            product = new Product(ID, new ProductName(args[1]), args[2], Double.parseDouble(args[3]));
        } else if (args.length == 4 && rawID == -1) {
            ProductID ID = catalog.getNewProductID();
            product = new Product(ID, new ProductName(args[0]), args[1], Double.parseDouble(args[2]), Integer.parseInt(args[3]));
        } else if (args.length == 5) {
            ProductID ID = new ProductID(rawID);
            product = new Product(ID, new ProductName(args[1]), args[2], Double.parseDouble(args[3]), Integer.parseInt(args[4]));
        } else
            throw new WrongNumberOfArgsException();

        return product;
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

    protected final Catalog catalog;
}
