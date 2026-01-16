package es.upm.etsisi.poo.Models.User;

import es.upm.etsisi.poo.AppExceptions.AppEntityNotFoundException;
import es.upm.etsisi.poo.AppExceptions.EntityAlreadyExistsException;
import es.upm.etsisi.poo.AppExceptions.InvalidAppIDException;
import es.upm.etsisi.poo.AppExceptions.NullAppEntityException;
import es.upm.etsisi.poo.Models.User.UserEnums.UserType;
import es.upm.etsisi.poo.Models.User.Users.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class UserRegister<T extends User> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    public UserRegister(UserType userType) {
        users = new HashMap<>();
        this.USER_TYPE = userType;
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

    public void addUser(T user) throws EntityAlreadyExistsException, InvalidAppIDException, NullAppEntityException {
        if (user == null) throw new NullAppEntityException(USER_TYPE.toString());

        if (users.containsKey(user.getId())) {
            throw new EntityAlreadyExistsException(USER_TYPE.toString(), user.getId());
        }

        if (!isValidId(user.getId())) throw new InvalidAppIDException();

        users.put(user.getId(), user);
    }

    public T getUser(String userId){
        return users.get(userId);
    }

    public int removeUser(String userId) throws AppEntityNotFoundException {
        if (!users.containsKey(userId))
            throw new AppEntityNotFoundException("User", userId);

        users.remove(userId);
        return 0;
    }

    public List<T> getUsers() {
        return users.values().stream().toList();
    }

    public int size() {
        return users.size();
    }


    public String getNewId() { return null; } // TODO maybe delete this method since just Cashier uses it
    public abstract boolean isValidId(String id);

    public final UserType USER_TYPE;
    private final HashMap<String, T> users;
}