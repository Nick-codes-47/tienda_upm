package es.upm.etsisi.poo.UserContainer;

public class User {
    public User(String id, String name, Email email) {
        this.name = name;
        this.id = id;
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public Email getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("{identificador: %s, nombre: %s, email: %s}", id, name, email);
    }

    protected final String name;
    protected final Email email;
    protected final String id; // TODO: make it a class and all identificable objects implement form identificable ?
}
