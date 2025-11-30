package es.upm.etsisi.poo.Actions.ProductHandlerActions.Add;

import es.upm.etsisi.poo.ProductContainer.ProductTypes.ProductEnums.EventType;

public class AddFood extends AddEvent {
    public static final String ID = "addFood";

    public AddFood() {
        super(EventType.FOOD, ID);
    }
}
