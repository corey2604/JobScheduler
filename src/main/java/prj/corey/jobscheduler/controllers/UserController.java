package prj.corey.jobscheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prj.corey.jobscheduler.models.User;
import prj.corey.jobscheduler.services.UserService;

import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path="/signUp", consumes = "application/json")
    public ResponseEntity signUp(@RequestBody User user) {
        boolean successfulSignup = userService.signUp(user);
        return ResponseEntity.ok(successfulSignup ? user : "Unable to create new user.");
    }

    @PostMapping(path="/logIn", consumes = "application/json")
    public ResponseEntity logIn(@RequestBody User user) {
        boolean successfulLogin = userService.logIn(user);
        return ResponseEntity.ok(successfulLogin ? user : "Unable to log in.");
    }

    @GetMapping(path="/current")
    public ResponseEntity getCurrentUser() {
        Optional<User> currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(currentUser.isPresent() ? currentUser.get().getUsername() : "Not logged in.");
    }

    @GetMapping(path="/current/logOut")
    public ResponseEntity logOutCurrentUser() {
        userService.logOut();
        return ResponseEntity.ok("Logged Out.");
    }
}
