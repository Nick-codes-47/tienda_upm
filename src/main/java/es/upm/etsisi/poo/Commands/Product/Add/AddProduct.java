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
     * @return -3 if the id is already in the catalog
     *         -1 if the catalog is full
     *         0 if all went well
     *         1 if id, price or maxPersonalizable were not valid
     *         2 if one of the arguments was invalid to create the product
     *         3 if they weren't enough arguments
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

        try {
            int rawID = -1;

            if ("1234567890".contains(args[0])) // only IDs start with numbers
                rawID = Integer.parseInt(args[0]);

            if (args.length == 2 && rawID == -1) {
                ServiceID ID = catalog.getNewServiceID();
                product = new ServiceProduct(ID, LocalDateTime.parse(args[0]), args[1]);
            } else if (args.length == 3 && rawID != -1) {
                ServiceID ID = new ServiceID(rawID);
                product = new ServiceProduct(ID, LocalDateTime.parse(args[1]), args[2]);
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
        } catch (DateTimeParseException e) {
            System.err.println("ERROR: the date MUST have the format: yyyy-MM-dd");
        }

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
