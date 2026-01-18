package es.upm.etsisi.poo.Models.User.Core;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.EntityAlreadyExistsException;
import es.upm.etsisi.poo.AppExceptions.NullAppEntityException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class UserRegister<T extends User> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final HashMap<String, T> users;

    public UserRegister() {
        users = new HashMap<>();
    }

    public void loadData(HashMap<String, T> loadedUsers) {
        if (loadedUsers != null) {
            this.users.clear();
            this.users.putAll(loadedUsers);
        }
    }

    // Necesitamos acceso al mapa para guardarlo
    public HashMap<String, T> getRawMap() {
        return this.users;
    }

    @Override
    public Iterator<T> iterator() {
        return users.values().iterator();
    }

    public void addUser(T user) throws EntityAlreadyExistsException, NullAppEntityException {
        if (user == null) throw new NullAppEntityException("User");

        if (users.containsKey(user.getId())) {
            throw new EntityAlreadyExistsException("User", user.getId());
        }

        users.put(user.getId(), user);
    }

    public T getUser(String userId){
        return users.get(userId);
    }

    public void removeUser(String userId) throws AppEntityNotFoundException {
        if (!users.containsKey(userId))
            throw new AppEntityNotFoundException("User", userId);

        users.remove(userId);
    }

    public List<T> getUsers() {
        return users.values().stream().toList();
    }

    public int size() {
        return users.size();
    }
}