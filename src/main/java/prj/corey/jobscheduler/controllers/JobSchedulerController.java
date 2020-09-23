package prj.corey.jobscheduler.controllers;

import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prj.corey.jobscheduler.models.ScheduledJob;
import prj.corey.jobscheduler.models.User;
import prj.corey.jobscheduler.services.schedulerService.SchedulerService;
import prj.corey.jobscheduler.services.userService.UserService;

import java.util.Optional;

@RestController
@RequestMapping(path = "/jobs")
public class JobSchedulerController {
    private static final ResponseEntity UNAUTHORIZED_RESPONSE =
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in.");

    private final SchedulerService schedulerService;
    private final UserService userService;

    public JobSchedulerController(SchedulerService schedulerService, UserService userService) {
        this.schedulerService = schedulerService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity getJobs() {
        Optional<User> optionalUser = userService.getCurrentUser();
        if (optionalUser.isPresent()) {
            String username = optionalUser.get().getUsername();
            try {
                return ResponseEntity.ok(schedulerService.getAllJobs(username));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return UNAUTHORIZED_RESPONSE;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity runJob(@RequestBody ScheduledJob scheduledJob) {
        Optional<User> optionalUser = userService.getCurrentUser();
        if (optionalUser.isPresent()) {
            String username = optionalUser.get().getUsername();
            try {
                schedulerService.runJob(scheduledJob, username);
                return ResponseEntity.ok(scheduledJob);
            } catch (SchedulerException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return UNAUTHORIZED_RESPONSE;
    }

    @PatchMapping("/{jobName}/stop")
    public ResponseEntity stopJob(@PathVariable String jobName) {
        Optional<User> optionalUser = userService.getCurrentUser();
        if (optionalUser.isPresent()) {
            String username = optionalUser.get().getUsername();
            try {
                schedulerService.stopJob(jobName, username);
                return ResponseEntity.ok(jobName + " paused");
            } catch (SchedulerException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return UNAUTHORIZED_RESPONSE;
    }

    @PatchMapping("/{jobName}/resume")
    public ResponseEntity resumeJob(@PathVariable String jobName) {
        Optional<User> optionalUser = userService.getCurrentUser();
        if (optionalUser.isPresent()) {
            String username = optionalUser.get().getUsername();
            try {
                schedulerService.resumeJob(jobName, username);
                return ResponseEntity.ok(jobName + " resumed execution");
            } catch (SchedulerException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return UNAUTHORIZED_RESPONSE;
    }

    @DeleteMapping("/{jobName}")
    public ResponseEntity deleteJob(@PathVariable String jobName) {
        Optional<User> optionalUser = userService.getCurrentUser();
        if (optionalUser.isPresent()) {
            String username = optionalUser.get().getUsername();
            try {
                schedulerService.deleteJob(jobName, username);
                return ResponseEntity.ok(jobName + " deleted");
            } catch (SchedulerException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return UNAUTHORIZED_RESPONSE;
    }
}
