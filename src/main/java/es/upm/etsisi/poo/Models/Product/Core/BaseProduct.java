package es.upm.etsisi.poo.Models.Product.Core;

import es.upm.etsisi.poo.Models.Core.Copyable;
import es.upm.etsisi.poo.Models.Ticket.Core.TicketRegistrable;

import java.io.Serializable;

public abstract class BaseProduct<T extends BaseProduct<T>>
        implements TicketRegistrable<T, EntryArgs>, Copyable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    abstract public ProductID getID();

    public boolean equals(Object other) {
        if (this == other) return true;
        else if (other == null || other.getClass() != this.getClass()) return false;
        BaseProduct<?> product = (BaseProduct<?>) other;
        return this.getID() == product.getID();
    }
}