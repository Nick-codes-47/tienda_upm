package es.upm.etsisi.poo.Models.User.UserEnums;

public enum UserType
{
    CASHIER("Cashier"),
    CLIENT("Client")
    ;

    private final String print;
    UserType(String print)
    {
        this.print = print;
    }

    @Override
    public String toString() {
        return print;
    }
}
