package es.upm.etsisi.poo.Commands.Product.Add;

import es.upm.etsisi.poo.Containers.Product.Catalog;
import es.upm.etsisi.poo.Containers.Product.ProductTypes.ProductEnums.EventType;

public class AddFood extends AddEvent {
    public static final String ID = "addFood";

    public AddFood(Catalog catalog) {
        super(catalog, EventType.FOOD, ID);
    }
}
