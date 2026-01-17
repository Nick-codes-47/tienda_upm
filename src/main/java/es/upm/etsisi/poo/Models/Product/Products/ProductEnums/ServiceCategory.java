package es.upm.etsisi.poo.Models.Product.Products.ProductEnums;

import es.upm.etsisi.poo.Models.Product.Products.ServiceProduct;

public enum ServiceCategory {
    LOGISTICS("Logistics"),
    SPECTACLE("Spectacle"),
    INSURANCE("Insurance")
    ;
    private String print;
    ServiceCategory(String print) {
        this.print = print;
    }

    public String getPrint() {
        return print;
    }

    public static String getServiceCategories() {
        StringBuilder result = new StringBuilder();
        addServiceCategories(result);
        return result.toString();
    }

    private static void addServiceCategories(StringBuilder result) {
        boolean coma = false;
        for (ServiceCategory serviceCategory : values()) {
            if (coma) {
                result.append(", ");
            }
            result.append(serviceCategory.name());
            coma = true;
        }
    }
}