package es.upm.etsisi.poo.Models.Ticket.Core;

public enum TicketType {
    COMMON("common"),
    COMPANY("company");

    private final String print;

    TicketType(String print) {
        this.print = print;
    }

    @Override
    public String toString() {
        return print;
    }
}
