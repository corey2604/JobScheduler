package prj.corey.jobscheduler.services.userService;

import prj.corey.jobscheduler.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static UserServiceImpl userService = null;
    // Temporary usage as there is no external storage - limits to one logged in user at a time
    private Optional<User> currentUser = Optional.empty();
    private List<User> users = new ArrayList<>();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        return userService;
    }

    @Override
    public boolean signUp(User user) {
        if (users.contains(user)) {
            return false;
        }
        users.add(user);
        currentUser = Optional.of(user);
        return true;
    }

    @Override
    public boolean logIn(User user) {
        boolean userExists = users.contains(user);
        if (userExists) {
            currentUser = Optional.of(user);
        }
        return userExists;
    }

    @Override
    public Optional<User> getCurrentUser() {
        return currentUser;
    }

    @Override
    public void logOut() {
        if (currentUser.isPresent()) {
            currentUser = Optional.empty();
        }
    }

}
