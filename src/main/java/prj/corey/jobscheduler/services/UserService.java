package prj.corey.jobscheduler.services;

import prj.corey.jobscheduler.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static UserService userService = null;
    // Temporary usage as there is no external storage - limits to one logged in user at a time
    private Optional<User> currentUser = Optional.empty();
    private List<User> users = new ArrayList<>();

    private UserService() {
    }

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public boolean signUp(User user) {
        if (users.contains(user)) {
            return false;
        }
        users.add(user);
        currentUser = Optional.of(user);
        return true;
    }

    public boolean logIn(User user) {
        boolean userExists = users.contains(user);
        if (userExists) {
            currentUser = Optional.of(user);
        }
        return userExists;
    }

    public Optional<User> getCurrentUser() {
        return currentUser;
    }

    public void logOut() {
        if (currentUser.isPresent()) {
            currentUser = Optional.empty();
        }
    }

}
