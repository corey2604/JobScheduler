package prj.corey.jobscheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import prj.corey.jobscheduler.jobs.PrintJob;
import prj.corey.jobscheduler.jobs.SystemCallJob;

import java.util.Date;

@SpringBootApplication
public class JobSchedulerApplication {
    private static final String PRINT_JOB_GROUP = "printJobGroup";
    private static final String SYSTEM_CALL_JOB_GROUP = "systemCallJobGroup";

    public static void main(String[] args) throws SchedulerException {
        //SpringApplication.run(JobSchedulerApplication.class, args);

        JobDetail printJob = createJob(PrintJob.class,"printJob", PRINT_JOB_GROUP, "Test message");
        Trigger printJobTrigger = createTrigger("printJobTrigger", PRINT_JOB_GROUP);

        JobDetail systemCallJob = createJob(SystemCallJob.class, "systemCallJob", SYSTEM_CALL_JOB_GROUP, "ls -a");
        Trigger systemCallTrigger = createTrigger("systemCallTrigger", SYSTEM_CALL_JOB_GROUP);

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(printJob, printJobTrigger);
        scheduler.scheduleJob(systemCallJob, systemCallTrigger);
    }

    private static JobDetail createJob(Class<? extends Job> jobClass, String jobName, String group, String content) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobName, group)
                .usingJobData(PrintJob.CONTENT, content)
                .build();
    }

    private static Trigger createTrigger(String triggerName, String group) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName, group)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(5).repeatForever())
                .build();
    }
}
