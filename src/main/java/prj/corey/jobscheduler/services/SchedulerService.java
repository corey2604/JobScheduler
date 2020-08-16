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
    private static SchedulerService schedulerService = null;
    private static final String JOB_GROUP = "jobSchedulerGroup";
    private static final String DATA_MAP_KEY = "content";

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


    public List<ScheduledJob> getAllJobs() throws SchedulerException {
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
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            scheduledJob.setJobStatus(triggerState.toString());
            scheduledJobs.add(scheduledJob);
        }
        return scheduledJobs;
    }

    public void runJob(ScheduledJob scheduledJob) throws SchedulerException {
        JobDetail job = createJob(scheduledJob.getType(), scheduledJob.getName(), scheduledJob.getContent());
        Trigger trigger = createTrigger(scheduledJob.getName() + "Trigger", scheduledJob.getStartTime(), scheduledJob.getEndTime());
        scheduler.scheduleJob(job, trigger);
    }

    public void stopJob(String jobName) throws SchedulerException {
        JobKey jobKey = getJobKeyFromName(scheduler, jobName);
        scheduler.interrupt(jobKey);
        scheduler.pauseJob(jobKey);
    }

    public void resumeJob(String jobName) throws SchedulerException {
        JobKey jobKey = getJobKeyFromName(scheduler, jobName);
        scheduler.resumeJob(jobKey);
    }

    public void deleteJob(String jobName) throws SchedulerException {
        JobKey jobKey = getJobKeyFromName(scheduler, jobName);
        scheduler.deleteJob(jobKey);
    }

    private JobKey getJobKeyFromName(Scheduler scheduler, String jobName) throws SchedulerException {
        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP))) {
            if (jobKey.getName().equals(jobName)) {
                return jobKey;
            }
        }
        throw new SchedulerException();
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
