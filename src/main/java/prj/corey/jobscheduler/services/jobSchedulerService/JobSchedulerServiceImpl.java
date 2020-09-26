package prj.corey.jobscheduler.services.jobSchedulerService;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;
import prj.corey.jobscheduler.jobs.AbstractJob;
import prj.corey.jobscheduler.models.Job;
import prj.corey.jobscheduler.models.JobType;

import java.util.*;

@Service("jobSchedulerService")
public class JobSchedulerServiceImpl implements JobSchedulerService {
    private static final String TRIGGER_SUFFIX = "Trigger";
    private static final String JOB_GROUP_PREFIX = "jobSchedulerGroup";
    private static final String DATA_MAP_KEY = "content";
    private final Scheduler scheduler;

    public JobSchedulerServiceImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public List<Job> getAllJobs(String username) throws SchedulerException {
        List<Job> jobs = new ArrayList<>();

        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(getUserJobGroup(username)))) {
            String jobName = jobKey.getName();

            Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
            Job job = new Job();
            job.setName(jobName);
            job.setType(JobType.valueOf(scheduler.getJobDetail(jobKey).getDescription()));
            job.setContent(scheduler.getJobDetail(jobKey).getJobDataMap().getString(DATA_MAP_KEY));
            job.setStartTime(trigger.getStartTime());
            job.setEndTime(trigger.getEndTime());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.toString());
            jobs.add(job);
        }
        return jobs;
    }

    @Override
    public void startJob(Job scheduledJob, String username) throws SchedulerException {
        JobDetail job = createJob(scheduledJob.getType(), scheduledJob.getName(), scheduledJob.getContent(), username);
        Trigger trigger = createTrigger(scheduledJob.getName(), scheduledJob.getStartTime(),
                scheduledJob.getEndTime(), username);
        scheduler.scheduleJob(job, trigger);
    }

    @Override
    public void stopJob(String jobName, String username) throws SchedulerException {
        Optional<JobKey> optionalJobKey = getJobKeyFromName(scheduler, jobName, username);
        if (optionalJobKey.isPresent()) {
            JobKey jobKey = optionalJobKey.get();
            scheduler.interrupt(jobKey);
            scheduler.pauseJob(jobKey);
        }
    }

    @Override
    public void resumeJob(String jobName, String username) throws SchedulerException {
        Optional<JobKey> optionalJobKey = getJobKeyFromName(scheduler, jobName, username);
        if (optionalJobKey.isPresent()) {
            scheduler.resumeJob(optionalJobKey.get());
        }
    }

    @Override
    public void deleteJob(String jobName, String username) throws SchedulerException {
        Optional<JobKey> optionalJobKey = getJobKeyFromName(scheduler, jobName, username);
        if (optionalJobKey.isPresent()) {
            scheduler.deleteJob(optionalJobKey.get());
        }
    }

    private Optional<JobKey> getJobKeyFromName(Scheduler scheduler, String jobName, String username) throws SchedulerException {
        Set<JobKey> jobKeysForUser = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(getUserJobGroup(username)));
        return jobKeysForUser.stream().filter(jobKey -> jobKey.getName().equals(jobName)).findFirst();
    }

    private JobDetail createJob(JobType jobType, String jobName, String content, String username) {
        return JobBuilder.newJob(jobType.getJobClass())
                .withDescription(jobType.name())
                .withIdentity(jobName, getUserJobGroup(username))
                .usingJobData(AbstractJob.CONTENT, content)
                .build();
    }

    private Trigger createTrigger(String jobName, Date startTime, Date endTime, String username) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobName + TRIGGER_SUFFIX, getUserJobGroup(username))
                .startAt(startTime)
                .endAt(endTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(5).repeatForever())
                .build();
    }

    private String getUserJobGroup(String username) {
        return String.format("%s-%s", JOB_GROUP_PREFIX, username);
    }
}
