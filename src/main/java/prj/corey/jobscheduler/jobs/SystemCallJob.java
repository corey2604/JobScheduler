package prj.corey.jobscheduler.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.io.IOException;

public class SystemCallJob extends AbstractJob {

    @Override
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
