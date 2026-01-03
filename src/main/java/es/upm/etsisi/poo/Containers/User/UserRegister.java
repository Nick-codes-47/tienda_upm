package es.upm.etsisi.poo.Containers.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class UserRegister<T extends User> implements Iterable<T> {

    public UserRegister(String userType) {
        users = new HashMap<String, T>();
        this.USER_TYPE = userType;
    }

    @Override
    public Iterator<T> iterator() {
        return users.values().iterator();
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

    public T getUser(String userId){
        return users.get(userId);
    }

    public int removeUser(String userId){
        if (!users.containsKey(userId))
            return 1;
        
        users.remove(userId);
        return 0;
    }

    public List<T> getUsers() {
        return users.values().stream().toList();
    }

    public int size() {
        return users.size();
    }


    public String getNewId() { return null; };
    public abstract boolean isValidId(String id);

    public final String USER_TYPE;
    private final HashMap<String, T> users;
}