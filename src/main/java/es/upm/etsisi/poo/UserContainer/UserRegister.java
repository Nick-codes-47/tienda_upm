package es.upm.etsisi.poo.UserContainer;

import java.util.HashMap;

public class UserRegister {

    public UserRegister() {
        users = new HashMap<String, User>();
    }

    public int addUser(String id, User user){
        if (id != null || user != null)
            return 1;
        if (users.containsKey(id))
            return 2;

        users.put(id, user);
        return 0;
    }

    public User getUser(String userId){
        return users.get(userId);
    }

    public int removeUser(String userId){
        if (!users.containsKey(userId))
            return 1;
        
        users.remove(userId);
        return 0;
    }

    public User[] getUsers() {
        return users.values().toArray(new User[0]);
    }

    public int size() {
        return users.size();
    }

    private final HashMap<String, User> users;
}