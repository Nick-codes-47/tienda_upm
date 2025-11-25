package es.upm.etsisi.poo.UserContainer;

public class User {
    public User(String name, Email email) {
        this.name = name;
        this.email = email;
        this.id = String.format("UW%07d", user_count);
        user_count++;
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

    protected final String name;
    protected final Email email;
    protected String id; // TODO: make it a class and all identificable objects implement form identificable ?

    private static int user_count = 0;
}
