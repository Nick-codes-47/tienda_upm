package es.upm.etsisi.poo.Commands.Product.Add;

import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.EventType;

public class AddFood extends AddEvent {
    public static final String ID = "addFood";

    public AddFood(Catalog catalog) {
        super(catalog, EventType.FOOD, ID);
    }
}
