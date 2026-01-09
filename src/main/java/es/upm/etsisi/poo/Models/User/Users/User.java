package es.upm.etsisi.poo.Models.User.Users;

import es.upm.etsisi.poo.Models.User.UserEnums.UserType;

public abstract class User {

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

    protected String addVarToPrint() {
        return "";
    }

    @Override
    public String toString() {
        UserType type;
        try {
            type = (UserType) getClass().getMethod("getType").invoke(null);
        } catch (Exception e) { type = null; } // This would never happen as User is an abstract class, subclasses MUST implement the static method getType
        assert type != null; // won't happen
        return String.format("%s{identifier='%s', name='%s', email:'%s'%s}", type, id, name, email, addVarToPrint());
    }

    protected final String name;
    protected final Email email;
    protected final String id;
}
