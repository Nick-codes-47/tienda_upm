package es.upm.etsisi.poo.Models.Product.Products.Core;

public class ServiceID extends ProductID {

    ServiceID(int ID) throws InvalidProductIDException {
        super(ID);
    }

    @Override
    public String toString() {
        return super.toString() + "S";
    }
}
