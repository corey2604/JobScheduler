package prj.corey.jobscheduler.controllers;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prj.corey.jobscheduler.models.ScheduledJob;
import prj.corey.jobscheduler.services.SchedulerService;

@RestController
@RequestMapping(path = "/jobs")
public class JobSchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @GetMapping()
    public ResponseEntity getJobs() {
        try {
            return ResponseEntity.ok(schedulerService.getAllJobs());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity runJob(@RequestBody ScheduledJob scheduledJob) {
        try {
            schedulerService.runJob(scheduledJob);
            return ResponseEntity.ok(scheduledJob);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PatchMapping("/{jobName}/stop")
    public ResponseEntity stopJob(@PathVariable String jobName) {
        try {
            schedulerService.stopJob(jobName);
            return ResponseEntity.ok(jobName + " paused");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/{jobName}/resume")
    public ResponseEntity resumeJob(@PathVariable String jobName) {
        try {
            schedulerService.resumeJob(jobName);
            return ResponseEntity.ok(jobName + " resumed execution");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{jobName}")
    public ResponseEntity deleteJob(@PathVariable String jobName) {
        try {
            schedulerService.deleteJob(jobName);
            return ResponseEntity.ok(jobName + " deleted");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
