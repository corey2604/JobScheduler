package prj.corey.jobscheduler.services.jobSchedulerService;

import org.quartz.SchedulerException;
import prj.corey.jobscheduler.models.Job;

import java.util.List;

public interface JobSchedulerService {

    List<Job> getAllJobs(String username) throws SchedulerException;

    void startJob(Job job, String username) throws SchedulerException;

    void stopJob(String jobName, String username) throws SchedulerException;

    void resumeJob(String jobName, String username) throws SchedulerException;

    void deleteJob(String jobName, String username) throws SchedulerException;

}
