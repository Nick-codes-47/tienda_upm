package es.upm.etsisi.poo.Containers.User;

public abstract class User {
    public static final String TYPE = "User"; // TODO: should be an enum

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

    public static String getType() { return TYPE; }

    protected String addVarToPrint() {
        return "";
    }

    @Override
    public String toString() {
        String type;
        try {
            type = (String)getClass().getMethod("getType").invoke(null);
        } catch (Exception e) { type = null; } // This would never happen as User is an abstract class, subclasses MUST implement the static method getType
        return String.format("%s{identifier='%s', name='%s', email:'%s'%s}", type, id, name, email, addVarToPrint());
    }

    protected final String name;
    protected final Email email;
    protected final String id;
}
