package es.upm.etsisi.poo.ProductContainer.ProductTypes.ProductEnums;

public enum EventType {
    FOOD(72), MEETING(12);

    EventType(int planningTime) {
        this.planningTime = planningTime;
    }
    public int getPlanningTime() { return this.planningTime; }

    public static String toSentenceCase(EventType eventType) {
        // Store in a String the eventType
        String type = eventType.name();

        // Convert to upper case the first character
        char firstChar = type.charAt(0);
        String firstletter = String.valueOf(firstChar).toUpperCase();

        // Convert to lower case the rest of the word
        String restOfWord = type.substring(1).toLowerCase();
        return firstletter + restOfWord;
    }

    private final int planningTime;
}
