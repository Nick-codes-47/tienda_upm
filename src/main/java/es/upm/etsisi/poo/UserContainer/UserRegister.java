package es.upm.etsisi.poo.UserContainer;

import java.util.HashMap;

public class UserRegister<T extends User> {

    public UserRegister() {
        users = new HashMap<String, T>();
    }

    public int addUser(T user){
        if (user == null)
            return 1;
        if (users.containsKey(user.getId())) {
            return 2;
        }

        if (!isValidId(user.getId()))
            return 3;

        users.put(user.getId(), user);
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

    public String getNewId() {
        String id;
        while (true) {
            id = String.format("UW%07d", nextIdVal++);
            if (getUser(id) == null)
                return id;
        }
    }

    public boolean isValidId(String id) {
        System.err.println("Por aqui");
        return id.length() == 9 && id.charAt(0) == 'U' && id.charAt(1) == 'W';
    }

    private final HashMap<String, T> users;
    private static int nextIdVal = 0;
}