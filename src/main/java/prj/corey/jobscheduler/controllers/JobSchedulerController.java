package prj.corey.jobscheduler.controllers;

import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prj.corey.jobscheduler.models.Job;
import prj.corey.jobscheduler.services.jobSchedulerService.JobSchedulerService;
import prj.corey.jobscheduler.services.userService.UserService;

import java.util.Optional;

@RestController
@RequestMapping(path = "/jobs")
public class JobSchedulerController {
    private static final ResponseEntity UNAUTHORIZED_RESPONSE =
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in.");

    private final JobSchedulerService jobSchedulerService;
    private final UserService userService;

    public JobSchedulerController(JobSchedulerService jobSchedulerService, UserService userService) {
        this.jobSchedulerService = jobSchedulerService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity getJobs() {
        Optional<String> optionalUsername = userService.getCurrentUsername();
        if (optionalUsername.isPresent()) {
            String username = optionalUsername.get();
            try {
                return ResponseEntity.ok(jobSchedulerService.getAllJobs(username));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return UNAUTHORIZED_RESPONSE;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity startJob(@RequestBody Job job) {
        Optional<String> optionalUsername = userService.getCurrentUsername();
        if (optionalUsername.isPresent()) {
            String username = optionalUsername.get();
            try {
                jobSchedulerService.startJob(job, username);
                return ResponseEntity.ok(job);
            } catch (SchedulerException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return UNAUTHORIZED_RESPONSE;
    }

    @PatchMapping("/{jobName}/stop")
    public ResponseEntity stopJob(@PathVariable String jobName) {
        Optional<String> optionalUsername = userService.getCurrentUsername();
        if (optionalUsername.isPresent()) {
            String username = optionalUsername.get();
            try {
                jobSchedulerService.stopJob(jobName, username);
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
        Optional<String> optionalUsername = userService.getCurrentUsername();
        if (optionalUsername.isPresent()) {
            String username = optionalUsername.get();
            try {
                jobSchedulerService.resumeJob(jobName, username);
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
        Optional<String> optionalUsername = userService.getCurrentUsername();
        if (optionalUsername.isPresent()) {
            String username = optionalUsername.get();
            try {
                jobSchedulerService.deleteJob(jobName, username);
                return ResponseEntity.ok(jobName + " deleted");
            } catch (SchedulerException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return UNAUTHORIZED_RESPONSE;
    }
}
