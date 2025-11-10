package es.upm.etsisi.poo.UserContainer;

import java.util.HashMap;

public class UserRegister {

    public UserRegister() {
        users = new HashMap<>();
    }

    public int addUser(User user){
        // TODO ALL
        return 0;
    }

    public User getUser(String userId){
        // TODO ALL
        return null;
    }

    public int removeUser(String userId){
        // TODO ALL
        return 0;
    }

    public int listUsers() {
        // TODO ALL
        return 0;
    }

    private HashMap<String, User> users;
}