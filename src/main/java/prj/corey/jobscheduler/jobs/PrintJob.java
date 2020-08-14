package prj.corey.jobscheduler.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

public class PrintJob implements Job {
    public static final String CONTENT = "No Content Supplied";

    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String message = dataMap.getString(CONTENT);
        System.out.println(message);
    }
}
