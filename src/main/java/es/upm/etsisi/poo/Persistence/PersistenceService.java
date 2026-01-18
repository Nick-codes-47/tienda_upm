package es.upm.etsisi.poo.Persistence;

import es.upm.etsisi.poo.AppLogger;

import java.io.*;
import java.util.HashMap;

public class PersistenceService {

    private final String PRODUCT_FILE = "products.dat";
    private final String CUSTOMER_FILE = "customers.dat";
    private final String CASHIER_FILE = "cashiers.dat";

    // Clase interna para manejar la lectura/escritura de objetos
    private static class FileHelper<T> {
        public void save(String filename, T data) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
                out.writeObject(data);
            } catch (IOException e) {
                AppLogger.error("Couldn't save " + filename + ": " + e.getMessage());
            }
        }

        @SuppressWarnings("unchecked")
        public T load(String filename) {
            File file = new File(filename);
            if (!file.exists()) return null;

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
                return (T) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                AppLogger.error("Couldn't load " + filename + ": " + e.getMessage());
                return null;
            }
        }
    }

    public void saveAll(HashMap<?, ?> catalogData, HashMap<?, ?> customerData, HashMap<?, ?> cashierData) {
        new FileHelper<HashMap<?, ?>>().save(PRODUCT_FILE, catalogData);
        new FileHelper<HashMap<?, ?>>().save(CUSTOMER_FILE, customerData);
        new FileHelper<HashMap<?, ?>>().save(CASHIER_FILE, cashierData);
        AppLogger.info("Data saved successfully.");
    }

    /**
     * Devuelve un array de 3 objetos [MapProductos, MapClientes, MapCajeros]
     */
    public Object[] loadAll() {
        Object products = new FileHelper<HashMap<?, ?>>().load(PRODUCT_FILE);
        Object customers = new FileHelper<HashMap<?, ?>>().load(CUSTOMER_FILE);
        Object cashiers = new FileHelper<HashMap<?, ?>>().load(CASHIER_FILE);

        return new Object[]{products, customers, cashiers};
    }
}