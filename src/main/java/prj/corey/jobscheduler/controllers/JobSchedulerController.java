package prj.corey.jobscheduler.controllers;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prj.corey.jobscheduler.jobs.AbstractJob;
import prj.corey.jobscheduler.models.ScheduledJob;
import prj.corey.jobscheduler.models.ScheduledJobType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/jobs")
public class JobSchedulerController {
    private static final String JOB_GROUP = "jobSchedulerGroup";
    private static final String DATA_MAP_KEY = "content";

    @GetMapping()
    public ResponseEntity getJobs() {
        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            List<ScheduledJob> scheduledJobs = new ArrayList<>();

            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP))) {
                String jobName = jobKey.getName();

                Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
                ScheduledJob scheduledJob = new ScheduledJob();
                scheduledJob.setName(jobName);
                scheduledJob.setType(ScheduledJobType.valueOf(scheduler.getJobDetail(jobKey).getDescription()));
                scheduledJob.setContent(scheduler.getJobDetail(jobKey).getJobDataMap().getString(DATA_MAP_KEY));
                scheduledJob.setStartTime(trigger.getStartTime());
                scheduledJob.setEndTime(trigger.getEndTime());
                scheduler.getJobDetail(jobKey).getJobClass();
                scheduledJobs.add(scheduledJob);
            }
            return ResponseEntity.ok(scheduledJobs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity runJob(@RequestBody ScheduledJob scheduledJob) {
        JobDetail job = createJob(scheduledJob.getType(), scheduledJob.getName(), scheduledJob.getContent());
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

    private JobDetail createJob(ScheduledJobType jobType, String jobName, String content) {
        return JobBuilder.newJob(jobType.getJobClass())
                .withDescription(jobType.name())
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
