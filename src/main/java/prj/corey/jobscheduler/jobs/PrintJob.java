package prj.corey.jobscheduler.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

public class PrintJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String message = dataMap.getString(CONTENT);
        System.out.println(message);
    }
}
