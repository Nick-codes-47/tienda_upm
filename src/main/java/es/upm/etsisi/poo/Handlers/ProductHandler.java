package es.upm.etsisi.poo.Handlers;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.Commands.Product.Add.AddFood;
import es.upm.etsisi.poo.Commands.Product.Add.AddMeeting;
import es.upm.etsisi.poo.Commands.Product.Add.AddProduct;
import es.upm.etsisi.poo.Commands.Product.ListProducts;
import es.upm.etsisi.poo.Commands.Product.RemoveProduct;
import es.upm.etsisi.poo.Commands.Product.UpdateProduct;

public class ProductHandler extends RequestHandler {
    public static String handler_id = "prod";

    public ProductHandler(App app) {
        super(handler_id);
        // We introduce all the actions that we'll need
        commands.put(AddProduct.ID, () -> new AddProduct(app.catalog));
        commands.put(UpdateProduct.ID, () -> new UpdateProduct(app.catalog, app.ticketService));
        commands.put(AddFood.ID, () -> new AddFood(app.catalog));
        commands.put(AddMeeting.ID, () -> new AddMeeting(app.catalog));
        commands.put(RemoveProduct.ID, () -> new RemoveProduct(app.catalog, app.ticketService));
        commands.put(ListProducts.ID, () -> new ListProducts(app.catalog));
    }
}
