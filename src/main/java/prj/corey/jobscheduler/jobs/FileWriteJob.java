package prj.corey.jobscheduler.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriteJob extends AbstractJob {
    private static final String FILE_NAME = "jobSchedulerFile.txt";

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String contentToWrite = dataMap.getString(CONTENT);
        try {
            File file = new File(FILE_NAME);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.printf("File \"%s\" already exists.%n", FILE_NAME);
            }
            FileWriter myWriter = new FileWriter(FILE_NAME);
            myWriter.write(contentToWrite);
            myWriter.close();
            System.out.println("Specified content has been written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
