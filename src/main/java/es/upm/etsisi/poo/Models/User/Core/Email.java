package es.upm.etsisi.poo.Models.User.Core;

import java.io.Serializable;

public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    public Email(String username, String domain) {
        this.username = username;
        this.domain = domain;
    }

    @Override
    public String toString() {
        return String.format("%s@%s", username, domain);
    }

    private final String username;
    private final String domain;
}