package es.upm.etsisi.poo.Commands.Product.Add;

import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.EventType;

public class AddMeeting extends AddEvent {
    public static final String ID = "addMeeting";

    public AddMeeting(Catalog catalog) { super(catalog, EventType.MEETING, ID); }
}
