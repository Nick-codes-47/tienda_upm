package es.upm.etsisi.poo;

public class Email {
    private String username;
    private String domain;

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
}
