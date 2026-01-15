package es.upm.etsisi.poo.Models.Product.Products.ProductEnums;

public enum Category {
    MERCH(0), STATIONERY(5), CLOTHES(7), BOOK(10), ELECTRONICS(3);

    Category(double discount) {
        this.discount = discount;
    }
    public double getDiscount() { return this.discount; }

    public static String getCategories() {
        StringBuilder result = new StringBuilder();
        addCategories(result);
        return result.toString();
    }

    private static void addCategories(StringBuilder result) {
        boolean coma = false;
        for (Category category : values()) {
            if (coma) {
                result.append(", ");
            }
            result.append(category.name());
            coma = true;
        }
    }

    public static String getCategoriesAndDiscount() {
        StringBuilder result = new StringBuilder();
        addCategoriesAndDiscounts(result);
        return result.toString();
    }

    private static void addCategoriesAndDiscounts(StringBuilder result) {
        boolean coma = false;
        for (Category category : values()) {
            if (coma) {
                result.append(", ");
            }
            int discountInt = (int) category.discount; // convert to int so we don't show decimals
            result.append(category.name()).append(" ").append(discountInt).append("%");
            coma = true;
        }
    }

    private final double discount;
}
