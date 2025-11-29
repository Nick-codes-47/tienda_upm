package es.upm.etsisi.poo.Requests.Handlers;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.Add.AddEvent;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.Add.AddProduct;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.ListProducts;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.RemoveProduct;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.UpdateProduct;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.ProductEnums.EventType;
import es.upm.etsisi.poo.Requests.RequestHandler;

import java.util.HashMap;

public class ProductHandler extends RequestHandler {
    public static String handler_id = "prod";

    public ProductHandler() {
        super(handler_id);
        // We introduce all the actions that we'll need
        actions.put("add", new AddProduct());
        actions.put("addFood", new AddEvent(EventType.FOOD));
        actions.put("addMeeting", new AddEvent(EventType.MEETING));
        actions.put("update",new UpdateProduct());
        actions.put("remove", new RemoveProduct());
        actions.put("list", new ListProducts());
    }
}
