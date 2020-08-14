package prj.corey.jobscheduler.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.io.IOException;

public class SystemCallJob implements Job {
    public static final String CONTENT = "No Content Supplied";

    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String systemCallCommand = dataMap.getString(CONTENT);
        try {
            Process process = Runtime.getRuntime().exec(systemCallCommand);
            while(process.isAlive()) {
                System.out.println("System call command is currently running: " + systemCallCommand);
            }
            System.out.println("Process completed with code: " + process.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
