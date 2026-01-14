package es.upm.etsisi.poo.Models.Product.Products.ProductEnums;

public enum EventType {
    FOOD(72, "Food"), MEETING(12, "Meeting");

    EventType(int planningTime, String print) {
        this.planningTime = planningTime;
        this.print = print;
    }
    public int getPlanningTime() { return this.planningTime; }

    @Override
    public String toString() {
        return this.print;
    }

    private final int planningTime;
    private final String print;
}
