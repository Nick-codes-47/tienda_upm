package es.upm.etsisi.poo.ProductContainer;

import java.time.LocalDateTime;

public class Event extends BaseProduct {

    public Event(int id, String name, double price, LocalDateTime expireDate, int maxPeople, EventType type)
        throws InvalidProductException{
        super(id,name, price);
        if (maxPeople < 1 || maxPeople > MAX_PEOPLE_ALLOWED)
            throw new InvalidProductException("ERROR: Events must have between 1 and 100 people");
        if (expireDate == null || expireDate.isBefore(LocalDateTime.now()))
            throw new InvalidProductException("ERROR: ExpireDate can't be null or before now");
        this.expireDate = expireDate;
        this.maxPeople = maxPeople;
        this.type = type;
    }

    public LocalDateTime getExpireDate() { return this.expireDate; }

    public EventType getType() { return this.type; }

    /**
     * Sets the actual people value to the one in the parameter. It checks
     * @param actualPeople the new value of actualPeople
     * @throws InvalidProductException Controls that the parameter actualPeople is valid
     */
    public void setActualPeople (int actualPeople) throws InvalidProductException {
        if (actualPeople > maxPeople) {
            throw new InvalidProductException("ERROR: There can't be more than "+maxPeople+" people");
        } else if (actualPeople < 1) {
            throw new InvalidProductException("ERROR: There must be at leat 1 person in the event");
        }
        this.actualPeople = actualPeople;
    }

    /**
     * Compares this product to another with the ID
     * @param obj (another)
     * @return whether the IDs match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Event other = (Event) obj;
        return super.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(super.getId());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{class:")
                .append(EventType.toSentenceCase(type))
                .append(", id:")
                .append(super.getId())
                .append(", name:'")
                .append(super.getName())
                .append("', price/person:")
                .append(super.getPrice())
                .append(", expiration date:")
                .append(this.expireDate.toLocalDate().toString())
                .append(", max people allowed:")
                .append(this.maxPeople);
        if (actualPeople != -1)
            sb.append(", actualPeople:").append(actualPeople);

        return sb.toString();
    }


    private final LocalDateTime expireDate;
    private final int maxPeople;
    private int actualPeople = -1;
    private final EventType type;
    private final int MAX_PEOPLE_ALLOWED = 100;
}
