package es.upm.etsisi.poo.Actions.UserHandlerActions;

import es.upm.etsisi.poo.App;
import es.upm.etsisi.poo.UserContainer.Email;
import es.upm.etsisi.poo.UserContainer.User;
import es.upm.etsisi.poo.UserContainer.UserRegister;

public class AddUser extends UserAction {

    public AddUser(App app, UserRegister userRegister) {
        super(app, userRegister);
    }

    public int execute(String[] args) {
        User user = createUser(args);
        String id = generateUsersId(user);
        
        return userRegister.addUser(id, user);
    }

    @Override
    public String help() {
        // TODO ALL
        return "";
    }

    User createUser(String[] args) {
        return new User(args[0], new Email(args[1]));
    }

    private String generateUsersId(User user) {
        int userCount = userRegister.size();
        return String.format("UW%07d", userCount);
    }
}
