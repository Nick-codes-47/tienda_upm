package es.upm.etsisi.poo.Models.Ticket;

import es.upm.etsisi.poo.Models.Product.Products.GoodsProduct;
import es.upm.etsisi.poo.Models.Product.Products.EventProduct;
import es.upm.etsisi.poo.Models.Product.Products.ProductEnums.EventType;

public class EventEntry extends TicketEntry<EventProduct> {

    public EventEntry(EventProduct event) {
        this.product = event;
    }

    /**
     * Sets the actual people value to the one in the parameter. It checks
     * @param actualPeople the new value of actualPeople
     * @throws GoodsProduct.InvalidProductException Controls that the parameter actualPeople is valid in the Event
     */
    public void setActualPeople (int actualPeople) throws GoodsProduct.InvalidProductException {
        if (actualPeople > product.getMaxPeople()) {
            throw new GoodsProduct.InvalidProductException(
                    "ERROR: There can't be more than "
                            + product.getMaxPeople() + " people"
            );
        } else if (actualPeople < 1) {
            throw new GoodsProduct.InvalidProductException(
                    "ERROR: There must be at leat 1 person in the event"
            );
        }
        people = actualPeople;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{class:")
                .append(product.getType().toSentenceCase())
                .append(", id:")
                .append(product.getId())
                .append(", name:'")
                .append(product.getName())
                .append("', price/person:")
                .append(product.getPrice())
                .append(", expiration date:")
                .append(product.getExpireDate().toLocalDate().toString())
                .append(", max people allowed:")
                .append(product.getMaxPeople())
                .append(", actual People in Event:")
                .append(people)
                .append("}");

        return sb.toString();
    }

    @Override
    public int getProductCount() {
        return 1;
    }

    @Override
    public double getPrice() {
        return product.getPrice() * people;
    }

    @Override
    public boolean checkValidity() {
        return product.getMaxPeople() >= people;
    }

    private int people = 0;
}
