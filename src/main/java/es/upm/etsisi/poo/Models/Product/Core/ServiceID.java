package es.upm.etsisi.poo.Models.Product.Core;

import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;

import java.io.Serializable;

public class ServiceID extends ProductID implements Serializable {

    private static final long serialVersionUID = 1L;

    public ServiceID(int id) throws InvalidAppIDException {
        super(id);
    }

    @Override
    public String toString() {
        return super.toString() + "S";
    }
}