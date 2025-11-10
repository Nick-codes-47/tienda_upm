package es.upm.etsisi.poo.Requests;

import es.upm.etsisi.poo.Actions.Action;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.Add.AddEvent;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.Add.AddProduct;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.ListProducts;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.RemoveProduct;
import es.upm.etsisi.poo.Actions.ProductHandlerActions.UpdateProduct;
import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.ProductContainer.EventType;

import java.util.HashMap;

public class ProductHandler extends RequestHandler{
    public ProductHandler(App app) {
        super();
        // We obtain the map of actions
        HashMap<String,Action> actions = super.getActions();
        // We introduce all the actions that we'll need
        actions.put("add", new AddProduct(app));
        actions.put("addFood", new AddEvent(app, EventType.FOOD));
        actions.put("addMeeting", new AddEvent(app, EventType.MEETING));
        actions.put("update",new UpdateProduct(app));
        actions.put("remove", new RemoveProduct(app));
        actions.put("list", new ListProducts(app));
    }
}
