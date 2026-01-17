package es.upm.etsisi.poo.Models.Product.Core;

public interface ExpirableProduct {
    boolean hasExpired();
    ProductID getID();
}
