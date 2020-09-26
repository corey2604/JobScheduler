package prj.corey.jobscheduler.services.userService;

import prj.corey.jobscheduler.models.User;

import java.util.Optional;

public interface UserService {

    boolean signUp(User user);

    boolean logIn(User user);

    Optional<String> getCurrentUsername();

    void logOut();
}
