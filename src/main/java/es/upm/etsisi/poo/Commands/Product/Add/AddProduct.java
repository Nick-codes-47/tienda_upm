package es.upm.etsisi.poo.Commands.Product.Add;

import es.upm.etsisi.poo.AppExceptions.InvalidDateFormatException;
import es.upm.etsisi.poo.AppExceptions.InvalidNumberFormatException;
import es.upm.etsisi.poo.AppExceptions.WrongNumberOfArgsException;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.Core.ProductName;
import es.upm.etsisi.poo.Models.Product.Core.ServiceID;
import es.upm.etsisi.poo.Models.Product.Products.Product.Product;
import es.upm.etsisi.poo.Models.Product.Products.Service.ServiceProduct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class to add a Product or CustomProduct to the catalog
 */
public class AddProduct implements Command {
    public static final String ID = "add";

    protected final Catalog catalog;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        if (args.length < 2) throw new WrongNumberOfArgsException(this);

        BaseProduct<?> product = createProduct(args);
        catalog.add(product);

        return 0;
    }

    protected BaseProduct<?> createProduct(String[] args) throws AppException {
        int rawID = -1;

        if (args[0].matches("-?\\d+"))
            rawID = Integer.parseInt(args[0]);

        ProductToAdd type = resolveProductToAdd(args, rawID);

        BaseProduct<?> product;
        try {
             product = getProduct(args, type, rawID);
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormatException(e.getMessage());
        }

        return product;
    }

    private ProductToAdd resolveProductToAdd(String[] args, int rawID) throws WrongNumberOfArgsException {
        return switch (args.length) {
            case 2 -> {
                if (rawID == -1) yield ProductToAdd.SERVICE;
                throw new WrongNumberOfArgsException(this);
            }
            case 3 -> {
                if (rawID == -1) yield ProductToAdd.PROD_WITHOUT_ID;
                throw new WrongNumberOfArgsException(this);
            }
            case 4 -> {
                if (rawID == -1) yield ProductToAdd.CUSTOM_WITHOUT_ID;
                yield ProductToAdd.PROD_WITH_ID;
            }
            case 5 -> ProductToAdd.CUSTOM_WITH_ID;
            default -> throw new WrongNumberOfArgsException(this);
        };
    }

    private BaseProduct<?> getProduct(String[] args, ProductToAdd type, int rawID) throws AppException {
        return switch (type) {
            case SERVICE -> {
                ServiceID id = catalog.getNewServiceID();
                try {
                    yield new ServiceProduct(id, LocalDate.parse(args[0], DATE_TIME_FORMATTER), args[1]);
                } catch (DateTimeParseException e) {
                    throw new InvalidDateFormatException();
                }
            }
            case PROD_WITHOUT_ID -> {
                ProductID id = catalog.getNewProductID();
                yield new Product(id, new ProductName(args[0]), args[1], Double.parseDouble(args[2]));
            }
            case PROD_WITH_ID -> {
                ProductID id = new ProductID(rawID);
                yield new Product(id, new ProductName(args[1]), args[2], Double.parseDouble(args[3]));
            }
            case CUSTOM_WITHOUT_ID -> {
                ProductID id = catalog.getNewProductID();
                yield new Product(
                        id,
                        new ProductName(args[0]),
                        args[1],
                        Double.parseDouble(args[2]),
                        Integer.parseInt(args[3])
                );
            }
            case CUSTOM_WITH_ID -> {
                ProductID id = new ProductID(rawID);
                yield new Product(
                        id,
                        new ProductName(args[1]),
                        args[2],
                        Double.parseDouble(args[3]),
                        Integer.parseInt(args[4])
                );
            }
        };
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

    private enum ProductToAdd {
        SERVICE, PROD_WITHOUT_ID, PROD_WITH_ID, CUSTOM_WITHOUT_ID, CUSTOM_WITH_ID
    }
}
