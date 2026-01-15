package es.upm.etsisi.poo.Models.Product.Products.Core;

import es.upm.etsisi.poo.Models.Core.AppID;

public class ProductID extends AppID {

    public ProductID(int ID) throws InvalidAppIDException {
        super(ID);
    }

    @Override
    public String toString() {
        return Integer.toString(baseID);
    }
}
