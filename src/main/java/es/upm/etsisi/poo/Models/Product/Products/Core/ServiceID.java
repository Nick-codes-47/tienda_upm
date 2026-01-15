package es.upm.etsisi.poo.Models.Product.Products.Core;

import java.io.Serializable;

public class ServiceID extends ProductID implements Serializable {

    private static final long serialVersionUID = 1L;

    public ServiceID(int ID) throws InvalidAppIDException {
        super(ID);
    }

    @Override
    public String toString() {
        return super.toString() + "S";
    }
}