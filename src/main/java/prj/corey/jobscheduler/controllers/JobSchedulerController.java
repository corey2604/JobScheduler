package prj.corey.jobscheduler.controllers;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prj.corey.jobscheduler.jobs.PrintJob;
import prj.corey.jobscheduler.models.ScheduledJob;

@RestController
public class JobSchedulerController {
    private static final String JOB_GROUP = "jobSchedulerGroup";

    @PostMapping(path = "/jobs", consumes = "application/json")
    public void runJob(@RequestBody ScheduledJob scheduledJob) {

        //TODO: Setting default job to print job for testing
        JobDetail job = createJob(scheduledJob.getType().getJobClass(), scheduledJob.getName(), JOB_GROUP, scheduledJob.getContent());
        Trigger trigger = createTrigger(scheduledJob.getName() + "Trigger", JOB_GROUP);
        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private JobDetail createJob(Class<? extends Job> jobClass, String jobName, String group, String content) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobName, group)
                .usingJobData(PrintJob.CONTENT, content)
                .build();
    }

    private Trigger createTrigger(String triggerName, String group) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName, group)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(5).repeatForever())
                .build();
    }
}
