package es.upm.etsisi.poo.Containers.Product.ProductTypes.ProductEnums;

public enum Category {
    MERCH(0), STATIONERY(5), CLOTHES(7), BOOK(10), ELECTRONICS(3);

    Category(double discount) {
        this.discount = discount;
    }
    public double getDiscount() { return this.discount; }

    public static String getCategories() {
        StringBuilder result = new StringBuilder();
        boolean coma = false;
        for (Category category : values()) {
            if (coma) {
                result.append(", ");
            }
            result.append(category.name());
            coma = true;
        }
        return result.toString();
    }

    public static String getCategoriesWithDiscount() {
        StringBuilder result = new StringBuilder();
        boolean coma = false;
        for (Category category : values()) {
            if (coma) {
                result.append(", ");
            }
            int discountInt = (int) category.discount; // convert to int so we don't show decimals
            result.append(category.name()).append(" ").append(discountInt).append("%");
            coma = true;
        }
        return result.toString();
    }

    private final double discount;
}
