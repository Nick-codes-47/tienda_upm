package es.upm.etsisi.poo;

public class User {
    private final String name;
    private Email email;

    public User(String name, Email email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public Email getEmail() {
        return email;
    }
}
