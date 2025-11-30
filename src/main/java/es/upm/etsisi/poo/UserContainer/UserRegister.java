package es.upm.etsisi.poo.UserContainer;

import java.util.HashMap;

public abstract class UserRegister<T extends User> {

    public UserRegister(String userType) {
        users = new HashMap<String, T>();
        this.USER_TYPE = userType;
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


    public String getNewId() { return null; };
    public abstract boolean isValidId(String id);

    public final String USER_TYPE;
    private final HashMap<String, T> users;
}