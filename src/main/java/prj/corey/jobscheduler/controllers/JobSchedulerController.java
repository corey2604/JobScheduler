package prj.corey.jobscheduler.controllers;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prj.corey.jobscheduler.jobs.PrintJob;
import prj.corey.jobscheduler.jobs.SystemCallJob;
import prj.corey.jobscheduler.jobs.WriteToFileJob;
import prj.corey.jobscheduler.models.ScheduledJob;

@RestController
public class JobSchedulerController {
    private static final String PRINT_JOB_GROUP = "printJobGroup";
    private static final String SYSTEM_CALL_JOB_GROUP = "systemCallJobGroup";
    private static final String WRITE_TO_FILE_JOB_GROUP = "writeToFileJobGroup";

    @PostMapping(path="/jobs", consumes="application/json")
    public String runJob(@RequestBody ScheduledJob scheduledJob) {

        //TODO: Setting default job to print job for testing
        JobDetail job = createJob(PrintJob.class,"printJob", PRINT_JOB_GROUP, "User job was not run");
        Trigger trigger = createTrigger("printJobTrigger", PRINT_JOB_GROUP);

        switch(scheduledJob.getType()) {
            case "print":
                job = createJob(PrintJob.class,"printJob", PRINT_JOB_GROUP, scheduledJob.getContent());
                trigger = createTrigger("printJobTrigger", PRINT_JOB_GROUP);
                break;
            case "system_call":
                job = createJob(SystemCallJob.class, "systemCallJob", SYSTEM_CALL_JOB_GROUP, scheduledJob.getContent());
                trigger = createTrigger("systemCallTrigger", SYSTEM_CALL_JOB_GROUP);
                break;
            case "write_to_file":
                job = createJob(WriteToFileJob.class, "writeToFileJob", WRITE_TO_FILE_JOB_GROUP, scheduledJob.getContent());
                trigger = createTrigger("writeToFileTrigger", WRITE_TO_FILE_JOB_GROUP);
                break;
            default:
                System.out.println("Unable to identify job to run");
                break;
        }

        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return "Test";
    }

    private JobDetail createJob(Class<? extends Job> jobClass, String jobName, String group, String content) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobName, group)
                .usingJobData(PrintJob.CONTENT, content)
                .build();
    }

    private Trigger createTrigger(String triggerName, String group) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName, group)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(5).repeatForever())
                .build();
    }
}
