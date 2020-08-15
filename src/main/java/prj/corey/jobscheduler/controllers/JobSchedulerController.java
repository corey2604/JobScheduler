package prj.corey.jobscheduler.controllers;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prj.corey.jobscheduler.jobs.AbstractJob;
import prj.corey.jobscheduler.models.ScheduledJob;

import java.util.Date;

@RestController
@RequestMapping(path = "/jobs")
public class JobSchedulerController {
    private static final String JOB_GROUP = "jobSchedulerGroup";

    @PostMapping(consumes = "application/json")
    public ResponseEntity runJob(@RequestBody ScheduledJob scheduledJob) {
        JobDetail job = createJob(scheduledJob.getType().getJobClass(), scheduledJob.getName(), scheduledJob.getContent());
        Trigger trigger = createTrigger(scheduledJob.getName() + "Trigger", scheduledJob.getStartTime(), scheduledJob.getEndTime());
        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
            return ResponseEntity.ok(scheduledJob);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    private JobDetail createJob(Class<? extends Job> jobClass, String jobName, String content) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobName, JOB_GROUP)
                .usingJobData(AbstractJob.CONTENT, content)
                .build();
    }

    private Trigger createTrigger(String triggerName, Date startTime, Date endTime) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName, JOB_GROUP)
                .startAt(startTime)
                .endAt(endTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(5).repeatForever())
                .build();
    }
}
