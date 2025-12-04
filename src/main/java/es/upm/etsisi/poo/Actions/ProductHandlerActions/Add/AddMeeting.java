package es.upm.etsisi.poo.Actions.ProductHandlerActions.Add;

import es.upm.etsisi.poo.Actions.UserHandlerActions.AddCustomer;
import es.upm.etsisi.poo.ProductContainer.ProductTypes.ProductEnums.EventType;

public class AddMeeting extends AddEvent {
    public static final String ID = "addMeeting";

    public AddMeeting() { super(EventType.MEETING, ID); }
}
