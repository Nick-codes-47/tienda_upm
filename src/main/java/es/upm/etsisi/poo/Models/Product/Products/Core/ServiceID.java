package es.upm.etsisi.poo.Models.Product.Products.Core;

import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;

import java.io.Serializable;

public class ServiceID extends ProductID implements Serializable {

    private static final long serialVersionUID = 1L;

    public ServiceID(int id) throws InvalidAppIDException {
        super(id);
    }

    public ServiceID(String id) throws InvalidAppIDException {
        super(id.substring(0, id.length() - 1));
    }

    @Override
    public String toString() {
        return super.toString() + "S";
    }
}