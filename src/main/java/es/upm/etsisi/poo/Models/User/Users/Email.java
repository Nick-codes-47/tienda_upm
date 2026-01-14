package es.upm.etsisi.poo.Models.User.Users;

public class Email {

    public Email(String username, String domain) {
        this.username = username;
        this.domain = domain;
    }

    public String getUsername() {
        return  username;
    }

    public String getDomain() {
        return domain;
    }

    @Override
    public String toString() {
        return String.format("%s@%s", username, domain);
    }

    private final String username;
    private final String domain;
}
