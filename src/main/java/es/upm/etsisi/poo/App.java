package es.upm.etsisi.poo;

import java.lang.reflect.Field;
import java.util.HashMap;

public class App
{
    Ticket currentTicket;
    HashMap<Integer, Product> products;
    Config config;

    public static void main( String[] args ) {
    }

    public void printTicket() {
        System.out.println(currentTicket.toString());
    }

    public void printProdList() {
        System.out.println("Catalog: ");
        for (Product product : products.values()) {
            System.out.println(" "+product.toString());
        }
    }

    /**
     * Method to update the fields of a product (NAME, CATEGORY, PRICE) in the catalog
     * @param id to search the product
     * @param field to know which field of the product needs to be changed
     * @param value the vnew value of the field
     * @return -1 if the product doesn't exist in the catalog.
     *          1 if the product exists but the field doesn't
     *          0 if the field was changed correctly
     */
    public int updateProduct(int id, String field, String value) {
        Product product = products.get(id);
        if (product == null) { return -1; }
        else {
            // We try to get the product's field to be modified
            try {
                Field f = Product.class.getDeclaredField(field);
                f.setAccessible(true);
                f.set(product, value);
            } catch (NoSuchFieldException e) {
                return 1; // if field doesn't exist we return 1
            } catch (IllegalAccessException e) {
                return 2; //
            }
        }
    }
}
