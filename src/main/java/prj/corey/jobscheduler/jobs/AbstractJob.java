package prj.corey.jobscheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public abstract class AbstractJob implements Job {
    public static final String CONTENT = "No Content Supplied";

    public abstract void execute(JobExecutionContext context);
}
