package prj.corey.jobscheduler.services;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import prj.corey.jobscheduler.jobs.AbstractJob;
import prj.corey.jobscheduler.models.ScheduledJob;
import prj.corey.jobscheduler.models.ScheduledJobType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SchedulerService {
    private static final String JOB_GROUP = "jobSchedulerGroup";
    private static final String DATA_MAP_KEY = "content";
    private static SchedulerService schedulerService = null;
    @Autowired
    private Scheduler scheduler;

    private SchedulerService() {
    }

    public static SchedulerService getInstance() {
        if (schedulerService == null) {
            schedulerService = new SchedulerService();
        }
        return schedulerService;
    }


    public List<ScheduledJob> getAllJobs(String username) throws SchedulerException {
        List<ScheduledJob> scheduledJobs = new ArrayList<>();

        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(buildUserJobGroup(username)))) {
            String jobName = jobKey.getName();

            Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
            ScheduledJob scheduledJob = new ScheduledJob();
            scheduledJob.setName(jobName);
            scheduledJob.setType(ScheduledJobType.valueOf(scheduler.getJobDetail(jobKey).getDescription()));
            scheduledJob.setContent(scheduler.getJobDetail(jobKey).getJobDataMap().getString(DATA_MAP_KEY));
            scheduledJob.setStartTime(trigger.getStartTime());
            scheduledJob.setEndTime(trigger.getEndTime());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            scheduledJob.setJobStatus(triggerState.toString());
            scheduledJobs.add(scheduledJob);
        }
        return scheduledJobs;
    }

    public void runJob(ScheduledJob scheduledJob, String username) throws SchedulerException {
        JobDetail job = createJob(scheduledJob.getType(), scheduledJob.getName(), scheduledJob.getContent(), username);
        Trigger trigger = createTrigger(scheduledJob.getName() + "Trigger", scheduledJob.getStartTime(),
                scheduledJob.getEndTime(), username);
        scheduler.scheduleJob(job, trigger);
    }

    public void stopJob(String jobName, String username) throws SchedulerException {
        JobKey jobKey = getJobKeyFromName(scheduler, jobName, username);
        scheduler.interrupt(jobKey);
        scheduler.pauseJob(jobKey);
    }

    public void resumeJob(String jobName, String username) throws SchedulerException {
        JobKey jobKey = getJobKeyFromName(scheduler, jobName, username);
        scheduler.resumeJob(jobKey);
    }

    public void deleteJob(String jobName, String username) throws SchedulerException {
        JobKey jobKey = getJobKeyFromName(scheduler, jobName, username);
        scheduler.deleteJob(jobKey);
    }

    private JobKey getJobKeyFromName(Scheduler scheduler, String jobName, String username) throws SchedulerException {
        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(buildUserJobGroup(username)))) {
            if (jobKey.getName().equals(jobName)) {
                return jobKey;
            }
        }
        throw new SchedulerException();
    }

    private JobDetail createJob(ScheduledJobType jobType, String jobName, String content, String username) {
        return JobBuilder.newJob(jobType.getJobClass())
                .withDescription(jobType.name())
                .withIdentity(jobName, buildUserJobGroup(username))
                .usingJobData(AbstractJob.CONTENT, content)
                .build();
    }

    private Trigger createTrigger(String triggerName, Date startTime, Date endTime, String username) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName, buildUserJobGroup(username))
                .startAt(startTime)
                .endAt(endTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(5).repeatForever())
                .build();
    }

    private String buildUserJobGroup(String username) {
        return String.format("%s-%s", JOB_GROUP, username);
    }
}
