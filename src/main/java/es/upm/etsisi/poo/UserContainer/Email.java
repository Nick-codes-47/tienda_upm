package es.upm.etsisi.poo.UserContainer;

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

    private final String username;
    private final String domain;
}
