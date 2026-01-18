package es.upm.etsisi.poo.Models.User.Core;

import es.upm.etsisi.poo.Models.User.UserEnums.UserType;

import java.io.Serializable;

public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final String name;
    protected final Email email;
    protected final String id;

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

    abstract public String toString();
}