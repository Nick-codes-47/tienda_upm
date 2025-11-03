package es.upm.etsisi.poo;

public enum EventType {
    FOOD(36), MEETING(12);

    private final int planningTime;

    EventType(int planningTime) {
        this.planningTime = planningTime;
    }
    public int getPlanningTime() { return this.planningTime; }
}
