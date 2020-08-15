package prj.corey.jobscheduler.models;

import prj.corey.jobscheduler.jobs.AbstractJob;
import prj.corey.jobscheduler.jobs.PrintJob;
import prj.corey.jobscheduler.jobs.SystemCallJob;
import prj.corey.jobscheduler.jobs.WriteToFileJob;

public enum ScheduledJobType {
    PRINT(PrintJob.class),
    SYSTEM_CALL(SystemCallJob.class),
    WRITE_TO_FILE(WriteToFileJob.class);

    Class<? extends AbstractJob> jobClass;

    ScheduledJobType(Class<? extends AbstractJob> jobClass) {
        this.jobClass = jobClass;
    }

    public Class<? extends AbstractJob> getJobClass() {
        return this.jobClass;
    }
}
