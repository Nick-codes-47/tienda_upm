package es.upm.etsisi.poo.Models.Product.Products.Core;

import es.upm.etsisi.poo.Models.Core.AppID;

import java.io.Serializable;

public class ProductID extends AppID implements Serializable {

    private static final long serialVersionUID = 1L;

    public ProductID(int ID) throws InvalidAppIDException {
        super(ID);
    }

    @Override
    public String toString() {
        return Integer.toString(baseID);
    }
}