package es.upm.etsisi.poo.Models.Product.Products.Core;

public class ServiceID extends ProductID {

    public ServiceID(int ID) throws InvalidAppIDException {
        super(ID);
    }

    @Override
    public String toString() {
        return super.toString() + "S";
    }
}
