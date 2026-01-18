package es.upm.etsisi.poo.Models.Ticket.Core;

import es.upm.etsisi.poo.AppExceptions.*;
import es.upm.etsisi.poo.AppExceptions.EntityExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.EntityExceptions.EntityAlreadyExistsException;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;

import java.io.Serializable;

public abstract class TicketEntry<ProductType extends BaseProduct<?>, EntryType extends TicketEntry<ProductType, EntryType>>
        implements Serializable {

    private static final long serialVersionUID = 1L;

    protected ProductType product;

    protected TicketEntry(ProductType product) {
        this.product = product;
    }

    // Es necesario el casteo a ProductType puesto que al ser BaseProduct<?>, la wildcard '?' no le permite garantizar
    // al compilador que se trata de la misma subclase que ProductType
    // realizamos el checkeo de que es el mismo tipo con la comprobación de que tienen el mismo ID
    // No podríamos sustituir ? por ProductType porque no nos permitiría tiner subclases de Ticket con diversos tipos
    // de producto como es el caso de CompanyTicket
    @SuppressWarnings("unchecked")
    public void update(ProductType newProduct) throws AppException {
        if (newProduct.getID().equals(product.getID())) {
            product = (ProductType) newProduct.copy();
        } else throw new AppEntityNotFoundException(newProduct.getClass().toString(), newProduct.getID().toString()); //TODO check getClass
    };

    public ProductType getProduct() { return product; };
    public abstract int getProductCount();
    public abstract double getPrice();

    public abstract void accumulate(EntryType more) throws EntityAlreadyExistsException;

    public abstract String toString();
}