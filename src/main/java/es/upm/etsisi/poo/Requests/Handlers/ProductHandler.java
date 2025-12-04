package es.upm.etsisi.poo.Requests.Handlers;

import es.upm.etsisi.poo.Actions.ProductHandlerActions.Add.AddFood;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.Add.AddMeeting;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.Add.AddProduct;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.ListProducts;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.RemoveProduct;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.UpdateProduct;
import es.upm.etsisi.poo.Requests.RequestHandler;

public class ProductHandler extends RequestHandler {
    public static String handler_id = "prod";

    public ProductHandler() {
        super(handler_id);
        // We introduce all the actions that we'll need
        actions.put(AddProduct.ID, new AddProduct());
        actions.put(UpdateProduct.ID,new UpdateProduct());
        actions.put(AddFood.ID, new AddFood());
        actions.put(AddMeeting.ID, new AddMeeting());
        actions.put(RemoveProduct.ID, new RemoveProduct());
        actions.put(ListProducts.ID, new ListProducts());
    }
}
