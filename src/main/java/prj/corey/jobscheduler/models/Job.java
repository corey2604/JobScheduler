package prj.corey.jobscheduler.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Job {
    private String name;
    private JobType type;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/London")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/London")
    private Date endTime;
    private String jobStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobType getType() {
        return type;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
}
