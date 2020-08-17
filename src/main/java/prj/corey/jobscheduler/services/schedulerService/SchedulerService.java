package prj.corey.jobscheduler.services.schedulerService;

import org.quartz.SchedulerException;
import prj.corey.jobscheduler.models.ScheduledJob;

import java.util.List;

public interface SchedulerService {

    List<ScheduledJob> getAllJobs(String username) throws SchedulerException;

    void runJob(ScheduledJob scheduledJob, String username) throws SchedulerException;

    void stopJob(String jobName, String username) throws SchedulerException;

    void resumeJob(String jobName, String username) throws SchedulerException;

    void deleteJob(String jobName, String username) throws SchedulerException;

}
